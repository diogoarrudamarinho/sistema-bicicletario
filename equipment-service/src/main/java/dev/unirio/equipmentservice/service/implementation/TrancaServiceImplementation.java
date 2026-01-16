package dev.unirio.equipmentservice.service.implementation;

import java.util.List;
import java.util.Objects;

import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.TrancaDTO;
import dev.unirio.equipmentservice.dto.TrancaIntegracaoDTO;
import dev.unirio.equipmentservice.dto.TrancaRequestDTO;
import dev.unirio.equipmentservice.entity.Bicicleta;
import dev.unirio.equipmentservice.entity.Totem;
import dev.unirio.equipmentservice.entity.Tranca;
import dev.unirio.equipmentservice.enumeration.BicicletaStatus;
import dev.unirio.equipmentservice.enumeration.TrancaStatus;
import dev.unirio.equipmentservice.exception.NegocioException;
import dev.unirio.equipmentservice.mapper.BicicletaMapper;
import dev.unirio.equipmentservice.mapper.TrancaMapper;
import dev.unirio.equipmentservice.repository.BicicletaRepository;
import dev.unirio.equipmentservice.repository.TotemRepository;
import dev.unirio.equipmentservice.repository.TrancaRepository;
import dev.unirio.equipmentservice.service.TrancaService;

@Service
public class TrancaServiceImplementation implements TrancaService {
    
    private final TrancaRepository repository;
    private final TrancaMapper mapper;
    
    private final BicicletaRepository bicicletaRepository;
    private final BicicletaMapper bicicletaMapper;
    private final TotemRepository totemRepository;

    private static final String NOT_FOUND = "Tranca não encontrada";
    private static final String ID_NULL = "ID é obrigatório";

    public TrancaServiceImplementation(TrancaRepository repository, TrancaMapper mapper,
            BicicletaRepository bicicletaRepository, TotemRepository totemRepository, BicicletaMapper bicicletaMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.bicicletaRepository = bicicletaRepository;
        this.totemRepository = totemRepository;
        this.bicicletaMapper = bicicletaMapper;
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
    public Tranca buscarEntidade(Long id) {
        if (id == null) 
            throw new NegocioException(ID_NULL);
        
        return repository.findById(id)
                .orElseThrow(() -> 
                new ObjectNotFoundException(NOT_FOUND, id));
    }

    @Override
    public BicicletaDTO buscarBicicleta(Long id) {
        if (id == null) 
            throw new NegocioException(ID_NULL);
    
        Tranca tranca = repository.findById(id)
                    .orElseThrow(() -> 
                    new ObjectNotFoundException(NOT_FOUND, id));

        if (tranca.getBicicleta() == null) 
            throw new NegocioException("Não existe bicicleta vinculada a esta tranca");
        
        return bicicletaMapper.toDto(tranca.getBicicleta());              
    }

    @Override
    public List<BicicletaDTO> buscarBicicletasPorTotem(Long id) {
        return repository.findAllByTotemId(id)
                .stream()
                .map(Tranca::getBicicleta) 
                .filter(Objects::nonNull)   
                .map(bicicletaMapper::toDto)
                .toList();
    }

    @Override
    public List<TrancaDTO> buscarTrancasPorTotem(Long id){
        return mapper.toDtoList(repository.findAllByTotemId(id));
    }

    @Override
    public TrancaDTO cadastrarTranca(TrancaRequestDTO novaTranca) {
        if (novaTranca.getStatus() != TrancaStatus.NOVA) 
            throw new NegocioException("Impossível criar tranca com status diferente de nova");

        Tranca tranca = mapper.toEntity(novaTranca);
    
        if (tranca == null) 
            throw new NegocioException("Tranca é obrigatória");

        return mapper.toDto(repository.save(tranca));
    }

    @Override
    @Transactional
    public TrancaDTO trancar(Long id, Long bicicletaId) {
        if (id == null) 
            throw new NegocioException(ID_NULL);

        Tranca tranca = repository.findById(id)
                        .orElseThrow(() -> 
                        new ObjectNotFoundException(NOT_FOUND, id));    

        if (bicicletaId != null){
            if (tranca.getStatus() == TrancaStatus.REPARO_SOLICITADO) 
                throw new NegocioException("Tranca com defeito");

            if (tranca.getBicicleta() != null) 
                throw new NegocioException("Tranca ocupada");

            Bicicleta bicicleta = bicicletaRepository.findById(bicicletaId)
                        .orElseThrow(() -> 
                        new ObjectNotFoundException("Bicicleta não encontrada", id));

            tranca.setBicicleta(bicicleta);
            
            bicicleta.alterarStatus(BicicletaStatus.DISPONIVEL);
            
            bicicletaRepository.save(bicicleta);
        }
        
        tranca.setStatus(TrancaStatus.OCUPADA);

        return mapper.toDto(repository.save(tranca));
    }

    @Override
    @Transactional
    public TrancaDTO destrancar(Long id, Long bicicletaId) {
        if (id == null) 
            throw new NegocioException(ID_NULL);

        Tranca tranca = repository.findById(id)
                        .orElseThrow(() -> 
                        new ObjectNotFoundException(NOT_FOUND, id));

        if (bicicletaId != null){

             Bicicleta bicicleta = bicicletaRepository.findById(bicicletaId)
                        .orElseThrow(() -> 
                        new ObjectNotFoundException("Bicicleta não encontrada", id));

            bicicleta.alterarStatus(BicicletaStatus.EM_USO);

            bicicletaRepository.save(bicicleta);
        }
        
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

        Tranca tranca = repository.findById(id)
                        .orElseThrow(() -> 
                        new ObjectNotFoundException(NOT_FOUND, id));

        /*
        * Se for retirar pra reparo ou aposentar: 
        *   - Tem que estar sem bicicleta 
        *   - Tem que ter reparo solicitado (em caso de reparo)
        */

        if (status == TrancaStatus.EM_REPARO && tranca.getStatus() != TrancaStatus.REPARO_SOLICITADO)
            throw new NegocioException("Tranca não tem reparo solicitado");

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

        if ((tranca.getStatus() != TrancaStatus.NOVA) && (tranca.getStatus() != TrancaStatus.EM_REPARO))
            throw new NegocioException("Tranca inválida"); 

        Totem totem = totemRepository.findById(request.getTotem())
                        .orElseThrow(() -> 
                        new ObjectNotFoundException("Totem não encontrado", request.getTotem()));
        tranca.setTotem(totem);
        tranca.setStatus(TrancaStatus.LIVRE);

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
