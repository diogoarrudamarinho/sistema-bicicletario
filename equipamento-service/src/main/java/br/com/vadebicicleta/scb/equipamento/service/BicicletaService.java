package br.com.vadebicicleta.scb.equipamento.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.vadebicicleta.scb.equipamento.dto.AlteraBicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.BicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.IntegrarNaRedeDTO;
import br.com.vadebicicleta.scb.equipamento.dto.NovaBicicletaDTO;
import br.com.vadebicicleta.scb.equipamento.dto.RetirarDaRedeDTO;
import br.com.vadebicicleta.scb.equipamento.entity.Bicicleta;
import br.com.vadebicicleta.scb.equipamento.entity.BicicletaStatus;
import br.com.vadebicicleta.scb.equipamento.entity.Tranca;
import br.com.vadebicicleta.scb.equipamento.entity.TrancaStatus;
import br.com.vadebicicleta.scb.equipamento.exception.RecursoNaoEncontradoException;
import br.com.vadebicicleta.scb.equipamento.exception.RegraDeNegocioException;
import br.com.vadebicicleta.scb.equipamento.mapper.BicicletaMapper;
import br.com.vadebicicleta.scb.equipamento.repository.BicicletaRepository;
import br.com.vadebicicleta.scb.equipamento.repository.TrancaRepository;

@Service
@Transactional
public class BicicletaService {

    private final BicicletaRepository bicicletaRepository;
    private final TrancaRepository trancaRepository;
    private final BicicletaMapper bicicletaMapper;

    private static final String BICICLETA_NAO_ENCONTRADA = "Bicicleta não encontrada com o ID: ";

    public BicicletaService(BicicletaRepository bicicletaRepository, TrancaRepository trancaRepository, BicicletaMapper bicicletaMapper) {
        this.bicicletaRepository = bicicletaRepository;
        this.trancaRepository = trancaRepository;
        this.bicicletaMapper = bicicletaMapper;
    }

    public BicicletaDTO cadastrarBicicleta(NovaBicicletaDTO dto) {
        Bicicleta bicicleta = bicicletaMapper.toEntity(dto);
        bicicleta.setPublicId(UUID.randomUUID());
        bicicleta.setStatus(BicicletaStatus.NOVA);

        Bicicleta bicicletaSalva = bicicletaRepository.save(bicicleta);
        return bicicletaMapper.toDto(bicicletaSalva);
    }

    public void retirarDaRede(RetirarDaRedeDTO dto) {
        Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                                .orElseThrow(() -> new RecursoNaoEncontradoException(BICICLETA_NAO_ENCONTRADA  + dto.getIdBicicleta()));
        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tranca não encontrada com o ID: " + dto.getIdTranca()));

        // Valida se a bicicleta está na tranca informada
        if (!tranca.getId().equals(bicicleta.getIdTranca())) {
            throw new RegraDeNegocioException("Bicicleta não está na tranca informada.");
        }

        // Valida o status da bicicleta
        if (bicicleta.getStatus() != BicicletaStatus.DISPONIVEL && bicicleta.getStatus() != BicicletaStatus.REPARO_SOLICITADO) {
            throw new RegraDeNegocioException("Bicicleta não pode ser retirada para reparo/aposentadoria, pois seu status atual é " + bicicleta.getStatus());
        }

        BicicletaStatus novoStatus;
        try {
            novoStatus = BicicletaStatus.valueOf(dto.getStatusAcaoReparador().toUpperCase());
            if (novoStatus != BicicletaStatus.EM_REPARO && novoStatus != BicicletaStatus.APOSENTADA) {
                throw new IllegalArgumentException(); // Vai capturar embaixo
            }
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioException("Ação inválida: " + dto.getStatusAcaoReparador() + ". Use 'EM_REPARO' ou 'APOSENTADA'.");
        }

        // Desassocia da tranca
        tranca.setBicicleta(null);
        tranca.setStatus(TrancaStatus.LIVRE);

        // Atualiza a bicicleta
        bicicleta.setStatus(novoStatus);
        bicicleta.setIdTranca(null);
        bicicleta.setIdTotem(null);

        trancaRepository.save(tranca);
        bicicletaRepository.save(bicicleta);
    }

    public void integrarNaRede(IntegrarNaRedeDTO dto) {
        Bicicleta bicicleta = bicicletaRepository.findById(dto.getIdBicicleta())
                                .orElseThrow(() -> new RecursoNaoEncontradoException(BICICLETA_NAO_ENCONTRADA  + dto.getIdBicicleta()));
        Tranca tranca = trancaRepository.findById(dto.getIdTranca())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tranca não encontrada com o ID: " + dto.getIdTranca()));

        // Validação das pré-condições do UC08
        if (bicicleta.getStatus() != BicicletaStatus.NOVA && bicicleta.getStatus() != BicicletaStatus.EM_REPARO) {
            throw new RegraDeNegocioException("Bicicleta só pode ser integrada se o status for NOVA ou EM_REPARO.");
        }
        if (tranca.getStatus() != TrancaStatus.LIVRE) {
            throw new RegraDeNegocioException("Tranca precisa estar com status LIVRE para receber uma bicicleta.");
        }

        // Realiza a integração
        tranca.setBicicleta(bicicleta);
        tranca.setStatus(TrancaStatus.OCUPADA);

        bicicleta.setStatus(BicicletaStatus.DISPONIVEL);
        bicicleta.setIdTranca(tranca.getId());
        if (tranca.getTotem() != null) {
            bicicleta.setIdTotem(tranca.getTotem().getId());
        }

        trancaRepository.save(tranca);
        bicicletaRepository.save(bicicleta);
    }

    public BicicletaDTO alterarStatus(Long id, String acao) {
        Bicicleta bicicleta = bicicletaRepository.findById(id)
                                .orElseThrow(() -> new RecursoNaoEncontradoException(BICICLETA_NAO_ENCONTRADA  + id));
        BicicletaStatus novoStatus;
        try {
            novoStatus = BicicletaStatus.valueOf(acao.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioException("Ação inválida: " + acao);
        }

        if (novoStatus == BicicletaStatus.APOSENTADA && bicicleta.getStatus() == BicicletaStatus.EM_USO) {
            throw new RegraDeNegocioException("Não é possível aposentar uma bicicleta em uso.");
        }

        bicicleta.setStatus(novoStatus);
        Bicicleta bicicletaAtualizada = bicicletaRepository.save(bicicleta);
        return bicicletaMapper.toDto(bicicletaAtualizada);
    }

    public void restaurarBanco() {
        bicicletaRepository.deleteAll();
    }

    public List<BicicletaDTO> listarTodas() {
        List<Bicicleta> bicicletas = bicicletaRepository.findAll();

        // Otimização para evitar múltiplas queries (problema N+1)
        List<Long> idsDeTrancas = bicicletas.stream()
                .map(Bicicleta::getIdTranca)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Map<Long, Tranca> mapaDeTrancas = trancaRepository.findAllById(idsDeTrancas).stream()
                .collect(Collectors.toMap(Tranca::getId, tranca -> tranca));

        return bicicletas.stream().map(bicicleta -> {
            BicicletaDTO dto = bicicletaMapper.toDto(bicicleta);
            if (bicicleta.getIdTranca() != null) {
                Tranca tranca = mapaDeTrancas.get(bicicleta.getIdTranca());
                if (tranca != null) {
                    dto.setIdTranca(tranca.getId());
                    if (tranca.getTotem() != null) {
                        dto.setIdTotem(tranca.getTotem().getId());
                    }
                }
            }
            return dto;
        }).collect(Collectors.toList());
    }


    public BicicletaDTO buscarPorId(Long id) {
        Bicicleta bicicleta = bicicletaRepository.findById(id)
                                .orElseThrow(() -> new RecursoNaoEncontradoException(BICICLETA_NAO_ENCONTRADA  + id));
        BicicletaDTO dto = bicicletaMapper.toDto(bicicleta);

        // Se a bicicleta estiver numa tranca, busca a tranca para obter os IDs públicos
        if (bicicleta.getIdTranca() != null) {
            trancaRepository.findById(bicicleta.getIdTranca()).ifPresent(tranca -> {
                dto.setIdTranca(tranca.getId());
                if (tranca.getTotem() != null) {
                    dto.setIdTotem(tranca.getTotem().getId());
                }
            });
        }
        return dto;
    }

    public BicicletaDTO alterarBicicleta(Long id, AlteraBicicletaDTO dto) {
        Bicicleta bicicleta = bicicletaRepository.findById(id)
                                .orElseThrow(() -> new RecursoNaoEncontradoException(BICICLETA_NAO_ENCONTRADA  + id));
        bicicletaMapper.updateEntityFromDto(dto, bicicleta);
        Bicicleta bicicletaAtualizada = bicicletaRepository.save(bicicleta);
        return bicicletaMapper.toDto(bicicletaAtualizada);
    }

    public void deletar(Long id) {
        Bicicleta bicicleta = bicicletaRepository.findById(id)
                                .orElseThrow(() -> new RecursoNaoEncontradoException(BICICLETA_NAO_ENCONTRADA  + id));
        if(bicicleta.getStatus() != BicicletaStatus.APOSENTADA || bicicleta.getIdTranca() != null) {
            throw new RegraDeNegocioException("Bicicleta só pode ser excluída se o status for APOSENTADA e não estiver em uma tranca.");
        }
        bicicletaRepository.delete(bicicleta);
    }
}
