package br.com.vadebicicleta.scb.equipamento.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
@Transactional
public class TotemService {

    private final TotemRepository totemRepository;
    private final TotemMapper totemMapper;
    private final TrancaMapper trancaMapper;
    private final BicicletaMapper bicicletaMapper;

    private static final String TOTEM_NAO_ENCONTRADO = "Totem não encontrado com o ID: ";

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
                .toList();
    }

    public TotemDTO buscarPorId(Long id) {
        return totemMapper.toDto(totemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(TOTEM_NAO_ENCONTRADO + id)));
    }

    public TotemDTO alterarTotem(Long id, AlteraTotemDTO dto) {
        Totem totem = totemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(TOTEM_NAO_ENCONTRADO + id));
        totemMapper.updateEntityFromDto(dto, totem);
        Totem totemAtualizado = totemRepository.save(totem);
        return totemMapper.toDto(totemAtualizado);
    }

    public void deletar(Long id) {
        Totem totem = totemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(TOTEM_NAO_ENCONTRADO + id));
        if (!totem.getTrancas().isEmpty()) {
            throw new RegraDeNegocioException("Não é possível remover um totem que possui trancas associadas.");
        }
        totemRepository.delete(totem);
    }

    public List<TrancaDTO> listarTrancasDoTotem(Long totemId) {
        Totem totem = totemRepository.findById(totemId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(TOTEM_NAO_ENCONTRADO + totemId));
        return totem.getTrancas().stream()
                .map(trancaMapper::toDto)
                .toList();
    }

    public List<BicicletaDTO> listarBicicletasDoTotem(Long totemId) {
        Totem totem = totemRepository.findById(totemId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(TOTEM_NAO_ENCONTRADO + totemId));
        return totem.getTrancas().stream()
                .filter(tranca -> tranca.getBicicleta() != null) // Garante que só processamos trancas com bicicletas
                .map(tranca -> {
                    Bicicleta bicicleta = tranca.getBicicleta();
                    // Faz o mapeamento básico da bicicleta
                    BicicletaDTO dto = bicicletaMapper.toDto(bicicleta);

                    // Enriquece o DTO com os IDs da sua localização
                    dto.setIdTranca(tranca.getId());
                    dto.setIdTotem(totem.getId()); // Já temos o totem, então usamos o seu ID

                    return dto;
                })
                .toList();
    }

    public void restaurarBanco() {
        totemRepository.deleteAll();
    }
}
