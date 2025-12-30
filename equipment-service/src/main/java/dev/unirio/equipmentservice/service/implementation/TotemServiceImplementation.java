package dev.unirio.equipmentservice.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.TotemDTO;
import dev.unirio.equipmentservice.dto.TotemRequestDTO;
import dev.unirio.equipmentservice.dto.TrancaDTO;
import dev.unirio.equipmentservice.entity.Totem;
import dev.unirio.equipmentservice.service.TotemService;

@Service
public class TotemServiceImplementation implements TotemService{
    
    @Override
    public TotemDTO buscarTotem(Long id){
        //TODO: implementação
        return null;
    }

    @Override
    public List<TotemDTO> buscarTotens(){
        //TODO: implementação
        return null;
    }

    @Override
    public List<TrancaDTO> buscarTrancas(Long id){
        //TODO: implementação
        return null;
    }

    @Override
    public List<BicicletaDTO> buscarBicicletas(Long id){
        //TODO: implementação
        return null;
    }

    @Override
    public TotemDTO criarTotem(TotemRequestDTO novoTotem){
        //TODO: implementação
        return null;
    }

    @Override
    public TotemDTO atualizarTotem(Long id, TotemRequestDTO totem){
        //TODO: implementação
        return null;
    }

    @Override
    public Void deletarTotem(Long id){
        //TODO: implementação
        return null;
    }
    
    @Override
    public Totem buscarEntidade(Long id){
        //TODO: implementação
        return null;
    }
}
