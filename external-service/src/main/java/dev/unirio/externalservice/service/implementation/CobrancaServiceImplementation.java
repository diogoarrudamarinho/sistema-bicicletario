package dev.unirio.externalservice.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import dev.unirio.externalservice.dto.CobrancaDTO;
import dev.unirio.externalservice.dto.CobrancaRequestDTO;
import dev.unirio.externalservice.service.CobrancaService;

@Service
public class CobrancaServiceImplementation implements CobrancaService{

    @Override
    public CobrancaDTO criarCobranca(CobrancaRequestDTO cobranca){
        return new CobrancaDTO();
    } 

    @Override
    public CobrancaDTO buscarCobranca(Long id){
        return new CobrancaDTO();
    } 

    @Override
    public List<CobrancaDTO> processarFilaCobranca() {
        return new ArrayList<>();
    }
}
