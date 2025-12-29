package dev.unirio.equipmentservice.service.implementation;

import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import dev.unirio.equipmentservice.dto.BicicletaDTO;
import dev.unirio.equipmentservice.dto.TrancaDTO;
import dev.unirio.equipmentservice.dto.TrancaRequestDTO;
import dev.unirio.equipmentservice.entity.Tranca;
import dev.unirio.equipmentservice.enumeration.BicicletaStatus;
import dev.unirio.equipmentservice.enumeration.TrancaStatus;
import dev.unirio.equipmentservice.mapper.TrancaMapper;
import dev.unirio.equipmentservice.repository.TrancaRepository;
import dev.unirio.equipmentservice.service.BicicletaService;

@Service
public class TrancaServiceImplementation /*implements TrancaService*/ {
    
    private TrancaRepository repository;
    private TrancaMapper mapper;

    private BicicletaService bicicletaService;

    //@Override
    public List<TrancaDTO> buscarTrancas() {
        return mapper.toDtoList(repository.findAll());
    }

    //@Override
    public TrancaDTO buscarTranca(Long id) {
        return id == null ?
        null:
        mapper.toDto(
            repository.findById(id)
                .orElseThrow(() -> 
                new ObjectNotFoundException("Tranca não encontrada", id)));
    }

    //@Override
    public BicicletaDTO buscarBicicleta(Long id) {
        if (id == null) 
            return null;
        
        Tranca tranca = repository.findById(id)
                        .orElseThrow(() -> 
                        new ObjectNotFoundException("Tranca não encontrada", id));

        return bicicletaService.buscarBicicleta(tranca.getBicicleta().getId());                    
    }

    //@Override
    public TrancaDTO cadastrarTranca(TrancaRequestDTO novaTranca) {
        Tranca tranca = mapper.toEntity(novaTranca);
    
        if (tranca == null) 
            return null;

        return mapper.toDto(repository.save(tranca));
    }

    //@Override
    public TrancaDTO trancar(Long id, Long bicicletaId) {
        if (id == null) 
            return null;

        if (bicicletaId != null) 
            bicicletaService.alterarStatus(id, BicicletaStatus.DISPONIVEL);
        
        return alterarStatus(id, TrancaStatus.OCUPADA);
    }

    //@Override
    public TrancaDTO destrancar(Long id, Long bicicletaId) {
        if (id == null) 
            return null;

        if (bicicletaId != null) 
            bicicletaService.alterarStatus(id, BicicletaStatus.DISPONIVEL);
        
        return alterarStatus(id, TrancaStatus.LIVRE);
    }

    //@Override
    public TrancaDTO alterarStatus(Long id, TrancaStatus status) {
        if (id == null) 
            return null;

        Tranca tranca = repository.findById(id)
                        .orElseThrow(() -> 
                        new ObjectNotFoundException("Tranca não encontrada", id));

        /*
        * Se for retirar pra reparo: 
        *   - Tem que estar sem bicicleta 
        *   - O reparo tem que ser solicitado previamente 
        */
        if (status == TrancaStatus.EM_REPARO) {
           
            if (tranca.getBicicleta() != null) {
                return null;
                //throw new Exception("erro");
            }

            if (tranca.getStatus() == TrancaStatus.REPARO_SOLICITADO) {
                return null;
                // throw new Exception
            }
        } 
        
        /*
        * Se for aposentar: 
        *   - Deve estar em em reparo 
        */
        if (status == TrancaStatus.APOSENTADA) {
            if (tranca.getStatus() == TrancaStatus.EM_REPARO) {
                return null;
                // throw new Exception
            }
        }
            
        tranca.setStatus(status);

        return mapper.toDto(repository.save(tranca));
    }
}
