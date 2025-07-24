package br.com.vadebicicleta.scb.equipamento.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        // Cria uma instância do validador que será usada em todos os testes
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("Testes para AlteraBicicletaDTO")
    class AlteraBicicletaDTOTest {
        @Test
        @DisplayName("Não deve ter violações com dados válidos")
        void quandoValido_NaoDeveTerViolacoes() {
            AlteraBicicletaDTO dto = new AlteraBicicletaDTO();
            dto.setMarca("Caloi");
            dto.setModelo("10");
            dto.setAno(2023);
            Set<ConstraintViolation<AlteraBicicletaDTO>> violations = validator.validate(dto);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Deve ter violação quando a marca for nula")
        void quandoMarcaNula_DeveTerViolacao() {
            AlteraBicicletaDTO dto = new AlteraBicicletaDTO();
            dto.setModelo("10");
            dto.setAno(2023);
            Set<ConstraintViolation<AlteraBicicletaDTO>> violations = validator.validate(dto);
            assertFalse(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes para IntegrarNaRedeDTO")
    class IntegrarNaRedeDTOTest {
        @Test
        @DisplayName("Não deve ter violações com dados válidos")
        void quandoValido_NaoDeveTerViolacoes() {
            IntegrarNaRedeDTO dto = new IntegrarNaRedeDTO();
            dto.setIdFuncionario(UUID.randomUUID());
            dto.setIdBicicleta(UUID.randomUUID());
            dto.setIdTranca(UUID.randomUUID());
            Set<ConstraintViolation<IntegrarNaRedeDTO>> violations = validator.validate(dto);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Deve ter violação quando idBicicleta for nulo")
        void quandoIdBicicletaNulo_DeveTerViolacao() {
            IntegrarNaRedeDTO dto = new IntegrarNaRedeDTO();
            dto.setIdFuncionario(UUID.randomUUID());
            dto.setIdTranca(UUID.randomUUID());
            Set<ConstraintViolation<IntegrarNaRedeDTO>> violations = validator.validate(dto);
            assertEquals(1, violations.size());
        }
    }

    @Nested
    @DisplayName("Testes para RetirarDaRedeDTO")
    class RetirarDaRedeDTOTest {
        @Test
        @DisplayName("Não deve ter violações com dados válidos")
        void quandoValido_NaoDeveTerViolacoes() {
            RetirarDaRedeDTO dto = new RetirarDaRedeDTO();
            dto.setIdFuncionario(UUID.randomUUID());
            dto.setIdBicicleta(UUID.randomUUID());
            dto.setIdTranca(UUID.randomUUID());
            dto.setStatusAcaoReparador("EM_REPARO");
            Set<ConstraintViolation<RetirarDaRedeDTO>> violations = validator.validate(dto);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Deve ter violação quando statusAcaoReparador for em branco")
        void quandoStatusEmBranco_DeveTerViolacao() {
            RetirarDaRedeDTO dto = new RetirarDaRedeDTO();
            dto.setIdFuncionario(UUID.randomUUID());
            dto.setIdBicicleta(UUID.randomUUID());
            dto.setIdTranca(UUID.randomUUID());
            dto.setStatusAcaoReparador(""); // Valor inválido
            Set<ConstraintViolation<RetirarDaRedeDTO>> violations = validator.validate(dto);
            assertFalse(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes para AlteraTotemDTO")
    class AlteraTotemDTOTest {
        @Test
        @DisplayName("Deve ter violação quando localização for em branco")
        void quandoLocalizacaoEmBranco_DeveTerViolacao() {
            AlteraTotemDTO dto = new AlteraTotemDTO();
            dto.setLocalizacao(""); // Valor inválido
            Set<ConstraintViolation<AlteraTotemDTO>> violations = validator.validate(dto);
            assertFalse(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes para AlteraTrancaDTO")
    class AlteraTrancaDTOTest {
        @Test
        @DisplayName("Deve ter violação quando anoDeFabricacao for nulo")
        void quandoAnoNulo_DeveTerViolacao() {
            AlteraTrancaDTO dto = new AlteraTrancaDTO();
            dto.setNumero("T001");
            dto.setModelo("Modelo Forte");
            dto.setAnoDeFabricacao(null); // Valor inválido
            Set<ConstraintViolation<AlteraTrancaDTO>> violations = validator.validate(dto);
            assertFalse(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes para NovaBicicletaDTO")
    class NovaBicicletaDTOTest {
        @Test
        @DisplayName("Deve ter violação quando ano for negativo")
        void quandoAnoNegativo_DeveTerViolacao() {
            NovaBicicletaDTO dto = new NovaBicicletaDTO();
            dto.setMarca("Caloi");
            dto.setModelo("10");
            dto.setAno(-2023); // Valor inválido
            Set<ConstraintViolation<NovaBicicletaDTO>> violations = validator.validate(dto);
            assertFalse(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes para NovaTrancaDTO")
    class NovaTrancaDTOTest {
        @Test
        @DisplayName("Deve ter violação quando modelo for em branco")
        void quandoModeloEmBranco_DeveTerViolacao() {
            NovaTrancaDTO dto = new NovaTrancaDTO();
            dto.setNumero("T002");
            dto.setModelo(""); // Valor inválido
            dto.setAnoDeFabricacao(2024);
            Set<ConstraintViolation<NovaTrancaDTO>> violations = validator.validate(dto);
            assertFalse(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes para NovoTotemDTO")
    class NovoTotemDTOTest {
        @Test
        @DisplayName("Deve ter violação quando localização for nula")
        void quandoLocalizacaoNula_DeveTerViolacao() {
            NovoTotemDTO dto = new NovoTotemDTO();
            dto.setLocalizacao(null); // Valor inválido
            Set<ConstraintViolation<NovoTotemDTO>> violations = validator.validate(dto);
            assertFalse(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes para TrancaIntegrarDTO")
    class TrancaIntegrarDTOTest {
        @Test
        @DisplayName("Deve ter violação quando idTotem for nulo")
        void quandoIdTotemNulo_DeveTerViolacao() {
            TrancaIntegrarDTO dto = new TrancaIntegrarDTO();
            dto.setIdFuncionario(UUID.randomUUID());
            dto.setIdTotem(null); // Valor inválido
            Set<ConstraintViolation<TrancaIntegrarDTO>> violations = validator.validate(dto);
            assertFalse(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes para TrancaRetirarDTO")
    class TrancaRetirarDTOTest {
        @Test
        @DisplayName("Deve ter violação quando idFuncionario for nulo")
        void quandoIdFuncionarioNulo_DeveTerViolacao() {
            TrancaRetirarDTO dto = new TrancaRetirarDTO();
            dto.setIdFuncionario(null); // Valor inválido
            dto.setStatusAcaoReparador("APOSENTADA");
            Set<ConstraintViolation<TrancaRetirarDTO>> violations = validator.validate(dto);
            assertFalse(violations.isEmpty());
        }
    }
}
