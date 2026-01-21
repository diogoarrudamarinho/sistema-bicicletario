package dev.unirio.rentalservice.service.implementation;

import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import dev.unirio.rentalservice.dto.FuncionarioDTO;
import dev.unirio.rentalservice.entity.Funcionario;
import dev.unirio.rentalservice.mapper.FuncionarioMapper;
import dev.unirio.rentalservice.repository.FuncionarioRepository;
import dev.unirio.rentalservice.service.FuncionarioService;

@Service
public class FuncionarioServiceImplementation implements FuncionarioService{
    
    private static final String NOT_FOUND = "Funcionário não encontrado";

    private final FuncionarioRepository repository;
    private final FuncionarioMapper mapper;

    public FuncionarioServiceImplementation(FuncionarioRepository repository, FuncionarioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public FuncionarioDTO buscarFuncionario(Long id){
        return mapper.toDto(repository.findById(id)
            .orElseThrow(() -> 
            new ObjectNotFoundException(NOT_FOUND, id)));
    }

    @Override
    public List<FuncionarioDTO> buscarFuncionarios() {
        return repository.findAll().stream()
            .map(mapper::toDto)
            .toList(); 
    }

    @Override
    public FuncionarioDTO criarFuncionario(FuncionarioDTO dto){
        return mapper.toDto(
                repository.save(
                    mapper.toEntity(dto)));
    }

    @Override
    public FuncionarioDTO atualizarFuncionario(Long id, FuncionarioDTO dto) {
        Funcionario funcionario = repository.findById(id)
                                  .orElseThrow(() -> 
                                    new ObjectNotFoundException(NOT_FOUND, id));

        mapper.updateEntityFromDto(dto, funcionario);

        return mapper.toDto(repository.save(funcionario));
    }

    @Override
    public void deletarFuncionario(Long id){
        if (!repository.existsById(id))
            throw new ObjectNotFoundException(NOT_FOUND, id);    
        
        repository.deleteById(id);
    }
}
