package br.com.vadebicicleta.scb.equipamento.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.vadebicicleta.scb.equipamento.dto.AlteraTrancaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.BicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.NovaTrancaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.TrancaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.TrancaIntegrarDTO;
import br.com.vadebicicleta.scb.equipamento.dto.TrancaRetirarDTO;
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

@Service
@Transactional
public class TrancaService {

    private final TrancaRepository trancaRepository;
    private final TotemRepository totemRepository;
    private final TrancaMapper trancaMapper;
    private final BicicletaMapper bicicletaMapper;
    private final BicicletaRepository bicicletaRepository;

    private static final String TRANCA_NAO_ENCONTRADA = "Tranca não encontrada com o ID: ";

    public TrancaService(BicicletaRepository bicicletaRepository, TrancaRepository trancaRepository, TotemRepository totemRepository, TrancaMapper trancaMapper, BicicletaMapper bicicletaMapper) {
        this.trancaRepository = trancaRepository;
        this.totemRepository = totemRepository;
        this.trancaMapper = trancaMapper;
        this.bicicletaMapper = bicicletaMapper;
        this.bicicletaRepository = bicicletaRepository;
    }

    public TrancaDTO cadastrarTranca(NovaTrancaDTO dto) {
        Tranca tranca = trancaMapper.toEntity(dto);
        tranca.setPublicId(UUID.randomUUID());
        tranca.setStatus(TrancaStatus.NOVA);
        return trancaMapper.toDto(trancaRepository.save(tranca));
    }

    public TrancaDTO alterarTranca(Long idTranca, AlteraTrancaDTO dto) {
        Tranca tranca = trancaRepository.findById(idTranca)
                        .orElseThrow(() -> new RecursoNaoEncontradoException(TRANCA_NAO_ENCONTRADA + idTranca));
        trancaMapper.updateEntityFromDto(dto, tranca);
        return trancaMapper.toDto(trancaRepository.save(tranca));
    }

    public void integrarNaRede(Long idTranca, TrancaIntegrarDTO dto) {
        Tranca tranca = trancaRepository.findById(idTranca)
                        .orElseThrow(() -> new RecursoNaoEncontradoException(TRANCA_NAO_ENCONTRADA + idTranca));
        Totem totem = totemRepository.findById(dto.getIdTotem())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Totem não encontrado."));

        if (tranca.getStatus() != TrancaStatus.NOVA && tranca.getStatus() != TrancaStatus.EM_REPARO) {
            throw new RegraDeNegocioException("Tranca só pode ser integrada se o status for NOVA ou EM_REPARO.");
        }
        tranca.setTotem(totem);
        tranca.setStatus(TrancaStatus.LIVRE);
        trancaRepository.save(tranca);
    }

    public void retirarDaRede(Long idTranca, TrancaRetirarDTO dto) {
        Tranca tranca = trancaRepository.findById(idTranca)
                        .orElseThrow(() -> new RecursoNaoEncontradoException(TRANCA_NAO_ENCONTRADA + idTranca));
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

    public TrancaDTO alterarStatus(Long idTranca, String acao) {
        Tranca tranca = trancaRepository.findById(idTranca)
                        .orElseThrow(() -> new RecursoNaoEncontradoException(TRANCA_NAO_ENCONTRADA + idTranca));
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
        return trancaRepository.findAll().stream().map(trancaMapper::toDto).toList();
    }

    public TrancaDTO buscarPorId(Long idTranca) {
        return trancaMapper.toDto(trancaRepository.findById(idTranca)
                        .orElseThrow(() -> new RecursoNaoEncontradoException(TRANCA_NAO_ENCONTRADA + idTranca)));
    }

    public BicicletaDTO obterBicicleta(Long idTranca) {
        Tranca tranca = trancaRepository.findById(idTranca)
                        .orElseThrow(() -> new RecursoNaoEncontradoException(TRANCA_NAO_ENCONTRADA + idTranca));
        if (tranca.getBicicleta() == null) {
            throw new RecursoNaoEncontradoException("Nenhuma bicicleta encontrada na tranca " + idTranca);
        }
        return bicicletaMapper.toDto(tranca.getBicicleta());
    }

    public void deletar(Long idTranca) {
        Tranca tranca = trancaRepository.findById(idTranca)
                        .orElseThrow(() -> new RecursoNaoEncontradoException(TRANCA_NAO_ENCONTRADA + idTranca));
        if (tranca.getStatus() == TrancaStatus.OCUPADA) {
            throw new RegraDeNegocioException("Não é possível remover tranca ocupada.");
        }
        trancaRepository.delete(tranca);
    }

    public TrancaDTO trancar(Long idTranca, Long idBicicleta) {
        Tranca tranca = trancaRepository.findById(idTranca)
                        .orElseThrow(() -> new RecursoNaoEncontradoException(TRANCA_NAO_ENCONTRADA + idTranca));
        if (tranca.getStatus() != TrancaStatus.LIVRE) {
            throw new RegraDeNegocioException("Tranca não está livre para ser trancada.");
        }
        tranca.setStatus(TrancaStatus.OCUPADA);
        tranca.setBicicleta(bicicletaRepository.findById(idBicicleta)
                        .orElseThrow(() -> new RecursoNaoEncontradoException("Bicicleta não encontrada com o ID: " + idBicicleta)));
        return trancaMapper.toDto(trancaRepository.save(tranca));
    }

    public TrancaDTO destrancar(Long idTranca) {
        Tranca tranca = trancaRepository.findById(idTranca)
                        .orElseThrow(() -> new RecursoNaoEncontradoException(TRANCA_NAO_ENCONTRADA + idTranca));
        if (tranca.getStatus() != TrancaStatus.OCUPADA && tranca.getStatus() != TrancaStatus.EM_REPARO) {
            throw new RegraDeNegocioException("Tranca não está ocupada para ser destrancada.");
        }
        tranca.setStatus(TrancaStatus.LIVRE);
        tranca.setBicicleta(null);
        return trancaMapper.toDto(trancaRepository.save(tranca));
    }

    public void restaurarBanco() {
        trancaRepository.deleteAll();
    }
}