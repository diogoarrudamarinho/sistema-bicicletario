package br.com.vadebicicleta.scb.equipamento.service;

import br.com.vadebicicleta.scb.equipamento.dto.AlteraTotemDTO;
import br.com.vadebicicleta.scb.equipamento.dto.BicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.NovoTotemDTO;
import br.com.vadebicicleta.scb.equipamento.dto.TotemDTO;
import br.com.vadebicicleta.scb.equipamento.dto.TrancaDTO;
import br.com.vadebicicleta.scb.equipamento.entity.Bicicleta;
import br.com.vadebicicleta.scb.equipamento.entity.Totem;
import br.com.vadebicicleta.scb.equipamento.exception.RecursoNaoEncontradoException;
import br.com.vadebicicleta.scb.equipamento.exception.RegraDeNegocioException;
import br.com.vadebicicleta.scb.equipamento.mapper.BicicletaMapper;
import br.com.vadebicicleta.scb.equipamento.mapper.TotemMapper;
import br.com.vadebicicleta.scb.equipamento.mapper.TrancaMapper;
import br.com.vadebicicleta.scb.equipamento.repository.TotemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TotemService {

    private final TotemRepository totemRepository;
    private final TotemMapper totemMapper;
    private final TrancaMapper trancaMapper;
    private final BicicletaMapper bicicletaMapper;

    public TotemService(TotemRepository totemRepository, TotemMapper totemMapper, TrancaMapper trancaMapper, BicicletaMapper bicicletaMapper) {
        this.totemRepository = totemRepository;
        this.totemMapper = totemMapper;
        this.trancaMapper = trancaMapper;
        this.bicicletaMapper = bicicletaMapper;
    }

    public TotemDTO cadastrarTotem(NovoTotemDTO dto) {
        Totem totem = totemMapper.toEntity(dto);
        totem.setPublicId(UUID.randomUUID());
        Totem totemSalvo = totemRepository.save(totem);
        return totemMapper.toDto(totemSalvo);
    }

    public List<TotemDTO> listarTodos() {
        return totemRepository.findAll().stream()
                .map(totemMapper::toDto)
                .collect(Collectors.toList());
    }

    public TotemDTO buscarPorId(UUID id) {
        return totemMapper.toDto(findTotemByPublicId(id));
    }

    public TotemDTO alterarTotem(UUID id, AlteraTotemDTO dto) {
        Totem totem = findTotemByPublicId(id);
        totemMapper.updateEntityFromDto(dto, totem);
        Totem totemAtualizado = totemRepository.save(totem);
        return totemMapper.toDto(totemAtualizado);
    }

    public void deletar(UUID id) {
        Totem totem = findTotemByPublicId(id);
        if (!totem.getTrancas().isEmpty()) {
            throw new RegraDeNegocioException("Não é possível remover um totem que possui trancas associadas.");
        }
        totemRepository.delete(totem);
    }

    public List<TrancaDTO> listarTrancasDoTotem(UUID totemId) {
        Totem totem = findTotemByPublicId(totemId);
        return totem.getTrancas().stream()
                .map(trancaMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BicicletaDTO> listarBicicletasDoTotem(UUID totemId) {
        Totem totem = findTotemByPublicId(totemId);
        return totem.getTrancas().stream()
                .filter(tranca -> tranca.getBicicleta() != null) // Garante que só processamos trancas com bicicletas
                .map(tranca -> {
                    Bicicleta bicicleta = tranca.getBicicleta();
                    // Faz o mapeamento básico da bicicleta
                    BicicletaDTO dto = bicicletaMapper.toDto(bicicleta);

                    // Enriquece o DTO com os IDs da sua localização
                    dto.setIdTranca(tranca.getPublicId());
                    dto.setIdTotem(totem.getPublicId()); // Já temos o totem, então usamos o seu ID

                    return dto;
                })
                .collect(Collectors.toList());
    }

    private Totem findTotemByPublicId(UUID id) {
        return totemRepository.findByPublicId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Totem não encontrado com o ID: " + id));
    }

    public void restaurarBanco() {
        totemRepository.deleteAll();
    }
}
