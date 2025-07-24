package br.com.vadebicicleta.scb.equipamento.service;

import br.com.vadebicicleta.scb.equipamento.dto.*;
import br.com.vadebicicleta.scb.equipamento.entity.Bicicleta;
import br.com.vadebicicleta.scb.equipamento.entity.Totem;
import br.com.vadebicicleta.scb.equipamento.entity.Tranca;
import br.com.vadebicicleta.scb.equipamento.entity.TrancaStatus;
import br.com.vadebicicleta.scb.equipamento.exception.RecursoNaoEncontradoException;
import br.com.vadebicicleta.scb.equipamento.exception.RegraDeNegocioException;
import br.com.vadebicicleta.scb.equipamento.mapper.BicicletaMapper;
import br.com.vadebicicleta.scb.equipamento.mapper.TrancaMapper;
import br.com.vadebicicleta.scb.equipamento.repository.BicicletaRepository;
import br.com.vadebicicleta.scb.equipamento.repository.TotemRepository;
import br.com.vadebicicleta.scb.equipamento.repository.TrancaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TrancaService {

    private final TrancaRepository trancaRepository;
    private final TotemRepository totemRepository;
    private final TrancaMapper trancaMapper;
    private final BicicletaMapper bicicletaMapper;

    public TrancaService(TrancaRepository trancaRepository, TotemRepository totemRepository, TrancaMapper trancaMapper, BicicletaMapper bicicletaMapper) {
        this.trancaRepository = trancaRepository;
        this.totemRepository = totemRepository;
        this.trancaMapper = trancaMapper;
        this.bicicletaMapper = bicicletaMapper;
    }

    public TrancaDTO cadastrarTranca(NovaTrancaDTO dto) {
        Tranca tranca = trancaMapper.toEntity(dto);
        tranca.setPublicId(UUID.randomUUID());
        tranca.setStatus(TrancaStatus.NOVA);
        return trancaMapper.toDto(trancaRepository.save(tranca));
    }

    public TrancaDTO alterarTranca(UUID idTranca, AlteraTrancaDTO dto) {
        Tranca tranca = findTrancaByPublicId(idTranca);
        trancaMapper.updateEntityFromDto(dto, tranca);
        return trancaMapper.toDto(trancaRepository.save(tranca));
    }

    public void integrarNaRede(UUID idTranca, TrancaIntegrarDTO dto) {
        Tranca tranca = findTrancaByPublicId(idTranca);
        Totem totem = totemRepository.findByPublicId(dto.getIdTotem())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Totem não encontrado."));

        if (tranca.getStatus() != TrancaStatus.NOVA && tranca.getStatus() != TrancaStatus.EM_REPARO) {
            throw new RegraDeNegocioException("Tranca só pode ser integrada se o status for NOVA ou EM_REPARO.");
        }
        tranca.setTotem(totem);
        tranca.setStatus(TrancaStatus.LIVRE);
        trancaRepository.save(tranca);
    }

    public void retirarDaRede(UUID idTranca, TrancaRetirarDTO dto) {
        Tranca tranca = findTrancaByPublicId(idTranca);
        if (tranca.getStatus() == TrancaStatus.OCUPADA) {
            throw new RegraDeNegocioException("Não é possível retirar uma tranca que está ocupada.");
        }

        TrancaStatus novoStatus;
        try {
            novoStatus = TrancaStatus.valueOf(dto.getStatusAcaoReparador().toUpperCase());
            if (novoStatus != TrancaStatus.EM_REPARO && novoStatus != TrancaStatus.APOSENTADA) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioException("Ação inválida: " + dto.getStatusAcaoReparador() + ". Use 'EM_REPARO' ou 'APOSENTADA'.");
        }

        tranca.setStatus(novoStatus);
        tranca.setTotem(null);
        trancaRepository.save(tranca);
    }

    public TrancaDTO alterarStatus(UUID idTranca, String acao) {
        Tranca tranca = findTrancaByPublicId(idTranca);
        // Lógica para trancar/destrancar
        if ("TRANCAR".equalsIgnoreCase(acao)) {
            tranca.setStatus(TrancaStatus.OCUPADA);
        } else if ("DESTRANCAR".equalsIgnoreCase(acao)) {
            tranca.setStatus(TrancaStatus.LIVRE);
        } else {
            throw new RegraDeNegocioException("Ação de status inválida: " + acao);
        }
        return trancaMapper.toDto(trancaRepository.save(tranca));
    }

    public List<TrancaDTO> listarTodas() {
        return trancaRepository.findAll().stream().map(trancaMapper::toDto).collect(Collectors.toList());
    }

    public TrancaDTO buscarPorId(UUID idTranca) {
        return trancaMapper.toDto(findTrancaByPublicId(idTranca));
    }

    public BicicletaDTO obterBicicleta(UUID idTranca) {
        Tranca tranca = findTrancaByPublicId(idTranca);
        if (tranca.getBicicleta() == null) {
            throw new RecursoNaoEncontradoException("Nenhuma bicicleta encontrada na tranca " + idTranca);
        }
        return bicicletaMapper.toDto(tranca.getBicicleta());
    }

    public void deletar(UUID idTranca) {
        Tranca tranca = findTrancaByPublicId(idTranca);
        if (tranca.getStatus() == TrancaStatus.OCUPADA) {
            throw new RegraDeNegocioException("Não é possível remover tranca ocupada.");
        }
        trancaRepository.delete(tranca);
    }

    private Tranca findTrancaByPublicId(UUID id) {
        return trancaRepository.findByPublicId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tranca não encontrada com o ID: " + id));
    }

    public void restaurarBanco() {
        trancaRepository.deleteAll();
    }
}