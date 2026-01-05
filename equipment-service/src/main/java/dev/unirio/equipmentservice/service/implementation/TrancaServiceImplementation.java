package dev.unirio.equipmentservice.service.implementation;

import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.TrancaDTO;
import dev.unirio.equipmentservice.dto.TrancaIntegracaoDTO;
import dev.unirio.equipmentservice.dto.TrancaRequestDTO;
import dev.unirio.equipmentservice.entity.Totem;
import dev.unirio.equipmentservice.entity.Tranca;
import dev.unirio.equipmentservice.enumeration.BicicletaStatus;
import dev.unirio.equipmentservice.enumeration.TrancaStatus;
import dev.unirio.equipmentservice.exception.NegocioException;
import dev.unirio.equipmentservice.mapper.TrancaMapper;
import dev.unirio.equipmentservice.repository.TrancaRepository;
import dev.unirio.equipmentservice.service.BicicletaService;
import dev.unirio.equipmentservice.service.TotemService;
import dev.unirio.equipmentservice.service.TrancaService;

@Service
public class TrancaServiceImplementation implements TrancaService {
    
    private final TrancaRepository repository;
    private final TrancaMapper mapper;
    private final BicicletaService bicicletaService;
    private final TotemService totemService;

    private static final String NOT_FOUND = "Tranca não encontrada";
    private static final String ID_NULL = "ID é obrigatório";

    public TrancaServiceImplementation(TrancaRepository repository, TrancaMapper mapper,
            BicicletaService bicicletaService, TotemService totemService) {
        this.repository = repository;
        this.mapper = mapper;
        this.bicicletaService = bicicletaService;
        this.totemService = totemService;
    }

    @Override
    public List<TrancaDTO> buscarTrancas() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public TrancaDTO buscarTranca(Long id) {
        if (id == null) 
            throw new NegocioException(ID_NULL);
        
        return mapper.toDto(
            repository.findById(id)
                .orElseThrow(() -> 
                new ObjectNotFoundException(NOT_FOUND, id)));
    }

    @Override
    public BicicletaDTO buscarBicicleta(Long id) {
        if (id == null) 
            throw new NegocioException(ID_NULL);
        
        Tranca tranca = repository.findById(id)
                        .orElseThrow(() -> 
                        new ObjectNotFoundException(NOT_FOUND, id));

        return bicicletaService.buscarBicicleta(tranca.getBicicleta().getId());                    
    }

    @Override
    public TrancaDTO cadastrarTranca(TrancaRequestDTO novaTranca) {
        Tranca tranca = mapper.toEntity(novaTranca);
    
        if (tranca == null) 
            throw new NegocioException("Tranca é obrigatória");

        return mapper.toDto(repository.save(tranca));
    }

    @Override
    public TrancaDTO trancar(Long id, Long bicicletaId) {
        if (id == null) 
            throw new NegocioException(ID_NULL);

        Tranca tranca = repository.findById(id)
                        .orElseThrow(() -> 
                        new ObjectNotFoundException(NOT_FOUND, id));
        
        if (bicicletaId != null) {
            bicicletaService.alterarStatus(bicicletaId, BicicletaStatus.DISPONIVEL);
            tranca.setBicicleta(bicicletaService.buscarEntidade(bicicletaId));
        }
        
        tranca.setStatus(TrancaStatus.OCUPADA);

        return mapper.toDto(repository.save(tranca));
    }

    @Override
    public TrancaDTO destrancar(Long id, Long bicicletaId) {
        if (id == null) 
            throw new NegocioException(ID_NULL);

        Tranca tranca = repository.findById(id)
                        .orElseThrow(() -> 
                        new ObjectNotFoundException(NOT_FOUND, id));

        if (bicicletaId != null) 
            bicicletaService.alterarStatus(bicicletaId, BicicletaStatus.EM_USO);
        

        tranca.setBicicleta(null);
        tranca.setStatus(TrancaStatus.LIVRE);

        return mapper.toDto(repository.save(tranca));
    }

    @Override
    public TrancaDTO alterarStatus(Long id, TrancaStatus status) {
        if (id == null) 
            throw new NegocioException(ID_NULL);

        // Só pode alterar para livre/ocupada através do método de trancar e destrancar

        if (status == TrancaStatus.LIVRE || status == TrancaStatus.OCUPADA)
            throw new NegocioException("Status Inválido para essa operação");
            // throw new Exception

        Tranca tranca = repository.findById(id)
                        .orElseThrow(() -> 
                        new ObjectNotFoundException(NOT_FOUND, id));

        /*
        * Se for retirar pra reparo ou aposentar: 
        *   - Tem que estar sem bicicleta 
        */
        if ((status == TrancaStatus.EM_REPARO || status == TrancaStatus.APOSENTADA) && tranca.getStatus() == TrancaStatus.OCUPADA) 
            throw new NegocioException("Tranca Ocupada");
        
        tranca.setStatus(status);

        return mapper.toDto(repository.save(tranca));
    }

    @Override
    public void integrarRede(TrancaIntegracaoDTO request) {

        if (request.getTotem() == null) 
            throw new NegocioException("Id do Totem é necessário nessa requisição");

        Tranca tranca = repository.findById(request.getTranca())
                        .orElseThrow(() -> 
                        new ObjectNotFoundException(NOT_FOUND, request.getTranca()));

        Totem totem = totemService.buscarEntidade(request.getTotem());
        tranca.setTotem(totem);

        repository.save(tranca);
    }

    @Override
    public void retirarRede(TrancaIntegracaoDTO request){
        if (request.getStatus() == null)
            throw new NegocioException("Status da tranca é necessária nessa requisição");

        Tranca tranca = repository.findById(request.getTranca())
                        .orElseThrow(() -> 
                        new ObjectNotFoundException(NOT_FOUND, request.getTranca()));

        if (tranca.getStatus() == TrancaStatus.OCUPADA)
            throw new NegocioException("Tranca está ocupada");

        tranca.setStatus(request.getStatus());
        tranca.setTotem(null);

        repository.save(tranca);
    }

    @Override
    public TrancaDTO atualizarTranca(Long id, TrancaRequestDTO novaTranca) {
        if (id == null || novaTranca == null) 
            throw new NegocioException(ID_NULL);

        Tranca trancaExistente = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(NOT_FOUND, id));

        mapper.updateEntityFromDto(novaTranca, trancaExistente);

        return mapper.toDto(repository.save(trancaExistente));
    }

    @Override
    public void deletar(Long id) {
        if (id == null) 
            throw new NegocioException(ID_NULL);

        if (!repository.existsById(id)) 
            throw new ObjectNotFoundException(NOT_FOUND, id);
    
        repository.deleteById(id);
    }
}
