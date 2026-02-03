package dev.unirio.rentalservice.service.implementation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dev.unirio.rentalservice.client.EquipmentClient;
import dev.unirio.rentalservice.client.ExternalClient;
import dev.unirio.rentalservice.dto.AluguelDTO;
import dev.unirio.rentalservice.dto.AluguelRequestDTO;
import dev.unirio.rentalservice.dto.BicicletaDTO;
import dev.unirio.rentalservice.dto.CiclistaDTO;
import dev.unirio.rentalservice.dto.CobrancaRequestDTO;
import dev.unirio.rentalservice.dto.CobrancaResponseDTO;
import dev.unirio.rentalservice.entity.Aluguel;
import dev.unirio.rentalservice.enumeration.BicicletaStatus;
import dev.unirio.rentalservice.mapper.AluguelMapper;
import dev.unirio.rentalservice.repository.AluguelRepository;
import dev.unirio.rentalservice.service.AluguelService;
import dev.unirio.rentalservice.service.CiclistaService;

@Service
public class AluguelServiceImplementation implements AluguelService {
    
    private final BigDecimal baseValue;
    private final AluguelRepository repository;
    private final EquipmentClient equipmentClient;
    public AluguelServiceImplementation(@Value("${aluguel.value}") BigDecimal baseValue, AluguelRepository repository,
            EquipmentClient equipmentClient, ExternalClient externalClient, CiclistaService ciclistaService,
            AluguelMapper mapper) {
        this.baseValue = baseValue;
        this.repository = repository;
        this.equipmentClient = equipmentClient;
        this.externalClient = externalClient;
        this.ciclistaService = ciclistaService;
        this.mapper = mapper;
    }

    private final ExternalClient externalClient;
    private final CiclistaService ciclistaService;
    private final AluguelMapper mapper;

    @Override
    public AluguelDTO alugar(AluguelRequestDTO request) {
        CiclistaDTO ciclista = ciclistaService.buscarCiclista(request.ciclista());
        if (repository.existsByCiclistaAndHoraFimIsNull(ciclista.id()))
            throw new IllegalArgumentException("Ciclista com aluguel ativo");

        BicicletaDTO bicicleta = equipmentClient.getBicicletaByTranca(request.tranca());
        CobrancaResponseDTO cobranca = externalClient.postRealizaCobranca(new CobrancaRequestDTO(ciclista.id(), baseValue));

        try {
           equipmentClient.postDestrancar(request.tranca(), bicicleta.id());
           return mapper.toDto(
                repository.save
                    (new Aluguel(
                        ciclista.id(),
                        bicicleta.id(),
                        request.tranca(),
                        cobranca.id()
                    )
                )
            );

        } catch (Exception e) {
            externalClient.postEstornarCobranca(cobranca.id());        
            equipmentClient.postAlterarStatusBicicleta(bicicleta.id(), BicicletaStatus.DISPONIVEL);
            throw new RuntimeException("Erro ao liberar tranca. A cobrança foi estornada.");
        }
    }

    @Override
    public AluguelDTO devolver(AluguelRequestDTO request) {
        Aluguel aluguel = repository.findByCiclistaAndHoraFimIsNull(request.ciclista())
                            .orElseThrow(() -> 
                            new ObjectNotFoundException("Aluguel não encontrado para ciclista", request.ciclista()));

        BicicletaDTO bicicleta = equipmentClient.getBicicletaByTranca(request.tranca());

        aluguel.setHoraFim(LocalDateTime.now());
        aluguel.setTrancaFim(request.tranca());

        BigDecimal extra = aluguel.calcularValorExcedente();
        CobrancaResponseDTO cobranca = null;

        if (extra.compareTo(BigDecimal.ZERO) > 0) 
            cobranca = externalClient.postRealizaCobranca(new CobrancaRequestDTO(request.ciclista(), extra));
        
        try {
            equipmentClient.postDestrancar(request.tranca(), bicicleta.id());
            Aluguel aluguelSalvo = repository.save(aluguel);

            return mapper.toDto(aluguelSalvo);
        } catch (Exception e) {
            if (cobranca != null) 
                externalClient.postEstornarCobranca(cobranca.id());

            throw new RuntimeException("Erro ao processar devolução. Cobranca estornada.");
        }
    }
}
