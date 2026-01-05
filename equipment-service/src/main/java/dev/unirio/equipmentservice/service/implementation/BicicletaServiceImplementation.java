package dev.unirio.equipmentservice.service.implementation;

import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.BicicletaRequestDTO;
import dev.unirio.equipmentservice.entity.Bicicleta;
import dev.unirio.equipmentservice.enumeration.BicicletaStatus;
import dev.unirio.equipmentservice.exception.NegocioException;
import dev.unirio.equipmentservice.mapper.BicicletaMapper;
import dev.unirio.equipmentservice.repository.BicicletaRepository;
import dev.unirio.equipmentservice.service.BicicletaService;

@Service
public class BicicletaServiceImplementation implements BicicletaService{
    
    private final BicicletaRepository repository;
    private final BicicletaMapper mapper;

    private static final String NOT_FOUND = "Bicicleta não encontrada";
    private static final String ID_NULL = "ID é obrigatório";

    public BicicletaServiceImplementation(BicicletaRepository repository,
                                          BicicletaMapper mapper
    )
    {
        this.repository = repository;
        this.mapper = mapper;
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
    public Bicicleta buscarEntidade(Long id){
        if (id == null)  
            throw new NegocioException(ID_NULL);

        return repository.findById(id)
                    .orElseThrow(() -> 
                    new ObjectNotFoundException(NOT_FOUND, id));
    }

    @Override
    public BicicletaDTO criarBicicleta(BicicletaRequestDTO novaBicicleta){
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

        bicicleta.setStatus(status);

        return mapper.toDto(repository.save(bicicleta));
    }

    @SuppressWarnings("null")
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

        repository.deleteById(id);
    }
}
