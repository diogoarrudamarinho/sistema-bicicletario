package dev.unirio.equipmentservice.service.implementation;

import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.BicicletaIntegracaoDTO;
import dev.unirio.equipmentservice.dto.BicicletaRequestDTO;
import dev.unirio.equipmentservice.entity.Bicicleta;
import dev.unirio.equipmentservice.enumeration.BicicletaStatus;
import dev.unirio.equipmentservice.exception.NegocioException;
import dev.unirio.equipmentservice.mapper.BicicletaMapper;
import dev.unirio.equipmentservice.repository.BicicletaRepository;
import dev.unirio.equipmentservice.service.BicicletaService;
import dev.unirio.equipmentservice.service.TrancaService;

@Service
public class BicicletaServiceImplementation implements BicicletaService{
    
    private final BicicletaRepository repository;
    private final BicicletaMapper mapper;
    private final TrancaService trancaService;

    private static final String NOT_FOUND = "Bicicleta não encontrada";
    private static final String ID_NULL = "ID é obrigatório";

    public BicicletaServiceImplementation(BicicletaRepository repository,
                                          BicicletaMapper mapper,
                                          TrancaService trancaService
    )
    {
        this.repository = repository;
        this.mapper = mapper;
        this.trancaService = trancaService;
    }

    @Override
    public List<BicicletaDTO> buscarBicicletas(){
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public BicicletaDTO buscarBicicleta(Long id){
        if (id == null)  
            throw new NegocioException(ID_NULL);
            
        return mapper.toDto(
            repository.findById(id)
                .orElseThrow(() -> 
                new ObjectNotFoundException(NOT_FOUND, id)));
    }

    @Override
    public BicicletaDTO criarBicicleta(BicicletaRequestDTO novaBicicleta){
        if (novaBicicleta.getStatus() != BicicletaStatus.NOVA) 
            throw new NegocioException("Impossível criar bicicleta com status diferente de nova");

        Bicicleta bicicleta = mapper.toEntity(novaBicicleta);
        if (bicicleta == null)
            throw new NegocioException(ID_NULL);
        
        return mapper.toDto(
            repository.save(bicicleta)
        );
    }

    @Override
    public BicicletaDTO alterarStatus(Long id, BicicletaStatus status) {
        if (id == null) 
            throw new NegocioException(ID_NULL);
        
        Bicicleta bicicleta = repository.findById(id)
                                .orElseThrow(() -> 
                                new ObjectNotFoundException(NOT_FOUND, id));
       
        bicicleta.alterarStatus(status);

        return mapper.toDto(repository.save(bicicleta));
    }

    @Override
    @Transactional
    public void integrarRede(BicicletaIntegracaoDTO request){
        if (request.getBicicleta() == null) 
            throw new NegocioException(ID_NULL);
        
        if (request.getTranca() == null)
            throw new NegocioException("ID da tranca é obrigatório");
        
        Bicicleta bicicleta = repository.findById(request.getBicicleta())
                                .orElseThrow(() -> 
                                new ObjectNotFoundException(NOT_FOUND, request.getBicicleta()));

        if (bicicleta.getTranca() != null)                 
            throw new NegocioException("Bicicleta já integrada na rede"); 

        if ((bicicleta.getStatus() != BicicletaStatus.NOVA) && (bicicleta.getStatus() != BicicletaStatus.EM_REPARO))
            throw new NegocioException("Bicicleta inválida"); 

        trancaService.trancar(request.getTranca(), request.getBicicleta());

        bicicleta.setTranca(trancaService.buscarEntidade(request.getTranca()));
        bicicleta.alterarStatus(BicicletaStatus.DISPONIVEL);

        repository.save(bicicleta);
    }

    @Override
    @Transactional
    public void retirarRede(BicicletaIntegracaoDTO request) {
        if (request.getBicicleta() == null) 
            throw new NegocioException(ID_NULL);

        if (request.getStatus() != BicicletaStatus.EM_REPARO && request.getStatus() != BicicletaStatus.APOSENTADA) 
            throw new NegocioException("Status inválido para essa operação");
        
        Bicicleta bicicleta = repository.findById(request.getBicicleta())
                                .orElseThrow(() -> 
                                new ObjectNotFoundException(NOT_FOUND, request.getBicicleta()));

        if (bicicleta.getTranca() == null)
            throw new NegocioException("Bicicleta já esta fora da rede");    

        if (bicicleta.getStatus() == BicicletaStatus.EM_USO)
            throw new NegocioException("Bicicleta inválida");        
        
        if (request.getStatus() == BicicletaStatus.EM_REPARO && bicicleta.getStatus() != BicicletaStatus.REPARO_SOLICITADO)
            throw new NegocioException("Reparo não foi solicitado");    

        trancaService.destrancar(bicicleta.getTranca().getId(), bicicleta.getId());

        bicicleta.setTranca(null);
        bicicleta.alterarStatus(request.getStatus());

        repository.save(bicicleta);
    }

    @Override
    public BicicletaDTO atualizarBicicleta(Long id, BicicletaRequestDTO novaBicicleta) {
        if (id == null)
            throw new NegocioException(ID_NULL);

        Bicicleta bicicleta = repository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException(NOT_FOUND, id));

        mapper.updateEntityFromDto(novaBicicleta, bicicleta);

        Bicicleta bicicletaAtualizada = repository.save(bicicleta);

        return mapper.toDto(bicicletaAtualizada);
    }

    @Override
    public void deletarBicicleta(Long id){
        if(id == null)
            throw new NegocioException(ID_NULL);

        if (!repository.existsById(id))
            throw new ObjectNotFoundException(NOT_FOUND, id);    

        repository.deleteById(id);
    }
}
