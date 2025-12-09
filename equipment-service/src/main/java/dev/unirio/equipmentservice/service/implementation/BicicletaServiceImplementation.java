package dev.unirio.equipmentservice.service.implementation;

import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.BicicletaRequestDTO;
import dev.unirio.equipmentservice.entity.Bicicleta;
import dev.unirio.equipmentservice.mapper.BicicletaMapper;
import dev.unirio.equipmentservice.repository.BicicletaRepository;
import dev.unirio.equipmentservice.service.BicicletaService;

@Service
public class BicicletaServiceImplementation implements BicicletaService{
    
    private final BicicletaRepository repository;
    private final BicicletaMapper mapper;

    public BicicletaServiceImplementation(BicicletaRepository repository,
                                          BicicletaMapper mapper
    )
    {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public BicicletaDTO buscarBicicleta(Long id){
        return id == null ? 
        null :
        mapper.toDto(
            repository.findById(id)
                .orElseThrow(() -> 
                new ObjectNotFoundException("Bicicleta não encontrada", id)));
    }

    @Override
    public BicicletaDTO criarBicicleta(BicicletaRequestDTO novaBicicleta){
        Bicicleta bicicleta = mapper.toEntity(novaBicicleta);
        if (bicicleta == null)
            return null;
        
        return mapper.toDto(
            repository.save(bicicleta)
        );
    }

    @SuppressWarnings("null")
    @Override
    public BicicletaDTO atualizarBicicleta(Long id, BicicletaRequestDTO novaBicicleta) {
        if (id == null)
            return null;

        Bicicleta bicicleta = repository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Bicicleta não encontrada", id));

        mapper.updateEntityFromDto(novaBicicleta, bicicleta);

        Bicicleta bicicletaAtualizada = repository.save(bicicleta);

        return mapper.toDto(bicicletaAtualizada);
    }

    @Override
    public void deletarBicicleta(Long id){
        if(id == null)
            return;
        repository.deleteById(id);
    }
}
