package dev.unirio.equipmentservice.service.implementation;

import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import dev.unirio.equipmentservice.dto.TotemDTO;
import dev.unirio.equipmentservice.dto.TotemRequestDTO;
import dev.unirio.equipmentservice.entity.Totem;
import dev.unirio.equipmentservice.exception.NegocioException;
import dev.unirio.equipmentservice.mapper.TotemMapper;
import dev.unirio.equipmentservice.repository.TotemRepository;
import dev.unirio.equipmentservice.service.TotemService;

@Service
public class TotemServiceImplementation implements TotemService{
    
    private final TotemRepository repository;
    private final TotemMapper mapper;
    
    public TotemServiceImplementation(TotemRepository repository, TotemMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    private static final String ID_NULO = "ID é obrigatótio";
    private static final String NOT_FOUND = "Totem não encontrado";

    @Override
    public TotemDTO buscarTotem(Long id){
        if (id == null) 
            throw new NegocioException(ID_NULO);    
        
        return mapper.toDto(
              repository.findById(id)
                        .orElseThrow(() -> 
                        new ObjectNotFoundException(NOT_FOUND, id)));  
    }

    @Override
    public List<TotemDTO> buscarTotens(){
        return mapper.toDtoList(repository.findAll());
    }

   @Override
    public TotemDTO criarTotem(TotemRequestDTO novoTotem) {
        if (novoTotem == null) 
            throw new NegocioException("Dados do totem são obrigatórios");

        Totem totem = mapper.toEntity(novoTotem);
        return mapper.toDto(repository.save(totem));
    }

    @Override
    public TotemDTO atualizarTotem(Long id, TotemRequestDTO totemDTO) {
        if (id == null) 
            throw new NegocioException(ID_NULO);

        Totem totemExistente = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(NOT_FOUND, id));

        mapper.updateEntityFromDto(totemDTO, totemExistente);

        return mapper.toDto(repository.save(totemExistente));
    }

    @Override
    public void deletarTotem(Long id) {
        if (id == null) 
            throw new NegocioException(ID_NULO);

        if (!repository.existsById(id)) 
            throw new ObjectNotFoundException(NOT_FOUND, id);
        
        repository.deleteById(id);
    }
}
