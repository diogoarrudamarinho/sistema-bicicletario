package br.com.vadebicicleta.scb.equipamento.dto;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class DtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        // Cria uma instância do validador que será usada em todos os testes
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    class TrancaDTOTest {

        @Test
        void testEqualsAndHashCode() {
            TrancaDTO t1 = new TrancaDTO();
            t1.setId(1L);
            t1.setNumero("001");
            t1.setModelo("Modelo X");
            t1.setAnoDeFabricacao(2022);
            t1.setStatus("LIVRE");
            t1.setIdTotem(10L);
            t1.setIdBicicleta(100L);

            TrancaDTO t2 = new TrancaDTO();
            t2.setId(1L);
            t2.setNumero("001");
            t2.setModelo("Modelo X");
            t2.setAnoDeFabricacao(2022);
            t2.setStatus("LIVRE");
            t2.setIdTotem(10L);
            t2.setIdBicicleta(100L);


            TrancaDTO t3 = new TrancaDTO();
            t3.setId(2L);
            t3.setNumero("002");
            t3.setModelo("Modelo Y");
            t3.setAnoDeFabricacao(2020);
            t3.setStatus("OCUPADA");
            t3.setIdTotem(11L);
            t3.setIdBicicleta(101L);


            // t1 e t2 devem ser iguais
            assertEquals(t1, t2);
            assertEquals(t1.hashCode(), t2.hashCode());

            // t1 e t3 devem ser diferentes
            assertNotEquals(t1, t3);
            assertNotEquals(t1.hashCode(), t3.hashCode());
        }
    }

    @Nested
    class ApiErrorResponseTest {

        @Test
        public void testGettersSettersAndConstructor() {
            String timestamp = "2025-07-28T10:00:00Z";
            int status = 404;
            String error = "Not Found";
            String message = "Recurso não encontrado";

            ApiErrorResponse response = new ApiErrorResponse(timestamp, status, error, message);

            assertEquals(timestamp, response.getTimestamp());
            assertEquals(status, response.getStatus());
            assertEquals(error, response.getError());
            assertEquals(message, response.getMessage());

            // Testando setters
            response.setTimestamp("2025-07-28T11:00:00Z");
            response.setStatus(400);
            response.setError("Bad Request");
            response.setMessage("Requisição inválida");

            assertEquals("2025-07-28T11:00:00Z", response.getTimestamp());
            assertEquals(400, response.getStatus());
            assertEquals("Bad Request", response.getError());
            assertEquals("Requisição inválida", response.getMessage());
        }

        @Test
        public void testEqualsAndHashCode() {
            ApiErrorResponse r1 = new ApiErrorResponse("2025-07-28T10:00:00Z", 500, "Internal Error", "Algo deu errado");
            ApiErrorResponse r2 = new ApiErrorResponse("2025-07-28T10:00:00Z", 500, "Internal Error", "Algo deu errado");
            ApiErrorResponse r3 = new ApiErrorResponse("2025-07-28T11:00:00Z", 400, "Bad Request", "Erro no input");

            assertEquals(r1, r2);
            assertEquals(r1.hashCode(), r2.hashCode());

            assertNotEquals(r1, r3);
            assertNotEquals(r1.hashCode(), r3.hashCode());
        }

        @Test
        public void testToString() {
            ApiErrorResponse response = new ApiErrorResponse("2025-07-28T10:00:00Z", 403, "Forbidden", "Acesso negado");

            String expected = "ApiErrorResponse(timestamp=2025-07-28T10:00:00Z, status=403, error=Forbidden, message=Acesso negado)";
            assertEquals(expected, response.toString());
        }
    }

    @Nested
    class IntegrarNaRedeDTOTest {

        @Test
        @DisplayName("Não deve ter violações com dados válidos")
        void quandoValido_NaoDeveTerViolacoes() {
            IntegrarNaRedeDTO dto = new IntegrarNaRedeDTO();
            dto.setIdFuncionario(1l);
            dto.setIdBicicleta(1l);
            dto.setIdTranca(1l);
            Set<ConstraintViolation<IntegrarNaRedeDTO>> violations = validator.validate(dto);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Deve ter violação quando idBicicleta for nulo")
        void quandoIdBicicletaNulo_DeveTerViolacao() {
            IntegrarNaRedeDTO dto = new IntegrarNaRedeDTO();
            dto.setIdFuncionario(1l);
            dto.setIdTranca(1l);
            Set<ConstraintViolation<IntegrarNaRedeDTO>> violations = validator.validate(dto);
            assertEquals(1, violations.size());
        }

        @Test
        public void testEqualsAndHashCode() {
            IntegrarNaRedeDTO dto1 = new IntegrarNaRedeDTO();
            dto1.setIdFuncionario(1L);
            dto1.setIdBicicleta(2L);
            dto1.setIdTranca(3L);

            IntegrarNaRedeDTO dto2 = new IntegrarNaRedeDTO();
            dto2.setIdFuncionario(1L);
            dto2.setIdBicicleta(2L);
            dto2.setIdTranca(3L);

            IntegrarNaRedeDTO dto3 = new IntegrarNaRedeDTO();
            dto3.setIdFuncionario(10L);
            dto3.setIdBicicleta(20L);
            dto3.setIdTranca(30L);

            // dto1 e dto2 devem ser iguais
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());

            // dto1 e dto3 devem ser diferentes
            assertNotEquals(dto1, dto3);
            assertNotEquals(dto1.hashCode(), dto3.hashCode());
        }

        @Test
        void testCanEqual() {
            IntegrarNaRedeDTO dto = new IntegrarNaRedeDTO();
            dto.setIdFuncionario(1L);
            dto.setIdBicicleta(2L);
            dto.setIdTranca(3L);

            Object other = new IntegrarNaRedeDTO();
            assertTrue(dto.canEqual(other));

            Object notSameType = new Object();
            assertFalse(dto.canEqual(notSameType));
        }

        @Test
        void testToString() {
            IntegrarNaRedeDTO dto = new IntegrarNaRedeDTO();
            dto.setIdFuncionario(1L);
            dto.setIdBicicleta(2L);
            dto.setIdTranca(3L);

            String expected = "IntegrarNaRedeDTO(idFuncionario=1, idBicicleta=2, idTranca=3)";
            assertEquals(expected, dto.toString());
        }
    }
    
    @Nested
    class RetirarDaRedeDTOTest {

        @Test
        void testEqualsAndHashCode() {
            RetirarDaRedeDTO r1 = new RetirarDaRedeDTO();
            r1.setIdFuncionario(1L);
            r1.setIdBicicleta(2L);
            r1.setIdTranca(3L);
            r1.setStatusAcaoReparador("EM_REPARO");

            RetirarDaRedeDTO r2 = new RetirarDaRedeDTO();
            r2.setIdFuncionario(1L);
            r2.setIdBicicleta(2L);
            r2.setIdTranca(3L);
            r2.setStatusAcaoReparador("EM_REPARO");

            RetirarDaRedeDTO r3 = new RetirarDaRedeDTO();
            r3.setIdFuncionario(99L);
            r3.setIdBicicleta(88L);
            r3.setIdTranca(77L);
            r3.setStatusAcaoReparador("APOSENTADA");

            // r1 e r2 devem ser iguais
            assertEquals(r1, r2);
            assertEquals(r1.hashCode(), r2.hashCode());

            // r1 e r3 devem ser diferentes
            assertNotEquals(r1, r3);
            assertNotEquals(r1.hashCode(), r3.hashCode());
        }

        @Test
        public void testCanEqual() {
            RetirarDaRedeDTO r1 = new RetirarDaRedeDTO();
            r1.setIdFuncionario(1L);
            r1.setIdBicicleta(2L);
            r1.setIdTranca(3L);
            r1.setStatusAcaoReparador("EM_REPARO");

            Object other = new RetirarDaRedeDTO();
            assertTrue(r1.canEqual(other));

            Object notSameType = new Object();
            assertFalse(r1.canEqual(notSameType));
        }

        @Test
        public void testToString() {
            RetirarDaRedeDTO r = new RetirarDaRedeDTO();
            r.setIdFuncionario(1L);
            r.setIdBicicleta(2L);
            r.setIdTranca(3L);
            r.setStatusAcaoReparador("EM_REPARO");

            String expected = "RetirarDaRedeDTO(idFuncionario=1, idBicicleta=2, idTranca=3, statusAcaoReparador=EM_REPARO)";
            assertEquals(expected, r.toString());
        }

        @Test
        @DisplayName("Não deve ter violações com dados válidos")
        void quandoValido_NaoDeveTerViolacoes() {
            RetirarDaRedeDTO dto = new RetirarDaRedeDTO();
            dto.setIdFuncionario(1l);
            dto.setIdBicicleta(1l);
            dto.setIdTranca(1l);
            dto.setStatusAcaoReparador("EM_REPARO");
            Set<ConstraintViolation<RetirarDaRedeDTO>> violations = validator.validate(dto);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Deve ter violação quando statusAcaoReparador for em branco")
        void quandoStatusEmBranco_DeveTerViolacao() {
            RetirarDaRedeDTO dto = new RetirarDaRedeDTO();
            dto.setIdFuncionario(1l);
            dto.setIdBicicleta(1l);
            dto.setIdTranca(1l);
            dto.setStatusAcaoReparador(""); // Valor inválido
            Set<ConstraintViolation<RetirarDaRedeDTO>> violations = validator.validate(dto);
            assertFalse(violations.isEmpty());
        }
    }
    @Nested
    class BicicletaDTOTest {

        @Test
        public void testEqualsAndHashCode() {
            BicicletaDTO b1 = new BicicletaDTO();
            b1.setId(1L);
            b1.setMarca("Caloi");
            b1.setModelo("Elite");
            b1.setAno(2023);
            b1.setStatus("DISPONIVEL");
            b1.setIdTranca(10L);
            b1.setIdTotem(100L);

            BicicletaDTO b2 = new BicicletaDTO();
            b2.setId(1L);
            b2.setMarca("Caloi");
            b2.setModelo("Elite");
            b2.setAno(2023);
            b2.setStatus("DISPONIVEL");
            b2.setIdTranca(10L);
            b2.setIdTotem(100L);

            BicicletaDTO b3 = new BicicletaDTO();
            b3.setId(2L);
            b3.setMarca("Monark");
            b3.setModelo("Classic");
            b3.setAno(2020);
            b3.setStatus("EM_USO");
            b3.setIdTranca(20L);
            b3.setIdTotem(200L);

            // b1 e b2 devem ser iguais
            assertEquals(b1, b2);
            assertEquals(b1.hashCode(), b2.hashCode());

            // b1 e b3 devem ser diferentes
            assertNotEquals(b1, b3);
            assertNotEquals(b1.hashCode(), b3.hashCode());
        }
    }

    @Nested
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

        @Test
        public void testEqualsAndHashCode() {
            AlteraTrancaDTO t1 = new AlteraTrancaDTO();
            t1.setNumero("001");
            t1.setModelo("Modelo X");
            t1.setAnoDeFabricacao(2022);

            AlteraTrancaDTO t2 = new AlteraTrancaDTO();
            t2.setNumero("001");
            t2.setModelo("Modelo X");
            t2.setAnoDeFabricacao(2022);

            AlteraTrancaDTO t3 = new AlteraTrancaDTO();
            t3.setNumero("002");
            t3.setModelo("Modelo Y");
            t3.setAnoDeFabricacao(2020);

            // t1 e t2 devem ser iguais
            assertEquals(t1, t2);
            assertEquals(t1.hashCode(), t2.hashCode());

            // t1 e t3 devem ser diferentes
            assertNotEquals(t1, t3);
            assertNotEquals(t1.hashCode(), t3.hashCode());
        }

        @Test
        public void testCanEqual() {
            AlteraTrancaDTO dto = new AlteraTrancaDTO();
            dto.setNumero("001");
            dto.setModelo("Modelo X");
            dto.setAnoDeFabricacao(2022);

            assertTrue(dto.canEqual(new AlteraTrancaDTO()));
            assertFalse(dto.canEqual(new Object()));
        }

        @Test
        public void testToString() {
            AlteraTrancaDTO dto = new AlteraTrancaDTO();
            dto.setNumero("001");
            dto.setModelo("Modelo X");
            dto.setAnoDeFabricacao(2022);

            String expected = "AlteraTrancaDTO(numero=001, modelo=Modelo X, anoDeFabricacao=2022)";
            assertEquals(expected, dto.toString());
        }
    }

    @Nested
    @DisplayName("Testes para AlteraBicicletaDTO")
    class AlteraBicicletaDTOTest {

        @Test
        public void testEqualsAndHashCode() {
            AlteraBicicletaDTO a1 = new AlteraBicicletaDTO();
            a1.setMarca("Caloi");
            a1.setModelo("Elite");
            a1.setAno(2023);

            AlteraBicicletaDTO a2 = new AlteraBicicletaDTO();
            a2.setMarca("Caloi");
            a2.setModelo("Elite");
            a2.setAno(2023);

            AlteraBicicletaDTO a3 = new AlteraBicicletaDTO();
            a3.setMarca("Monark");
            a3.setModelo("Classic");
            a3.setAno(2020);

            // a1 e a2 devem ser iguais
            assertEquals(a1, a2);
            assertEquals(a1.hashCode(), a2.hashCode());

            // a1 e a3 devem ser diferentes
            assertNotEquals(a1, a3);
            assertNotEquals(a1.hashCode(), a3.hashCode());
        }

        @Test
        public void testCanEqual() {
            AlteraBicicletaDTO dto = new AlteraBicicletaDTO();
            dto.setMarca("Caloi");
            dto.setModelo("Elite");
            dto.setAno(2023);

            assertTrue(dto.canEqual(new AlteraBicicletaDTO()));
            assertFalse(dto.canEqual(new Object()));
        }

        @Test
        public void testToString() {
            AlteraBicicletaDTO dto = new AlteraBicicletaDTO();
            dto.setMarca("Caloi");
            dto.setModelo("Elite");
            dto.setAno(2023);

            String expected = "AlteraBicicletaDTO(marca=Caloi, modelo=Elite, ano=2023)";
            assertEquals(expected, dto.toString());
        }

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
    @DisplayName("Testes para AlteraTotemDTO")
    class AlteraTotemDTOTest {
        @Test
        public void testEqualsAndHashCode() {
            AlteraTotemDTO a1 = new AlteraTotemDTO();
            a1.setLocalizacao("Praça Central");
            a1.setDescricao("Totem principal");

            AlteraTotemDTO a2 = new AlteraTotemDTO();
            a2.setLocalizacao("Praça Central");
            a2.setDescricao("Totem principal");

            AlteraTotemDTO a3 = new AlteraTotemDTO();
            a3.setLocalizacao("Rua 2");
            a3.setDescricao("Totem reserva");

            // a1 e a2 devem ser iguais
            assertEquals(a1, a2);
            assertEquals(a1.hashCode(), a2.hashCode());

            // a1 e a3 devem ser diferentes
            assertNotEquals(a1, a3);
            assertNotEquals(a1.hashCode(), a3.hashCode());
        }

        @Test
        public void testCanEqual() {
            AlteraTotemDTO dto = new AlteraTotemDTO();
            dto.setLocalizacao("Praça Central");
            dto.setDescricao("Totem principal");

            assertTrue(dto.canEqual(new AlteraTotemDTO()));
            assertFalse(dto.canEqual(new Object()));
        }

        @Test
        public void testToString() {
            AlteraTotemDTO dto = new AlteraTotemDTO();
            dto.setLocalizacao("Praça Central");
            dto.setDescricao("Totem principal");

            String expected = "AlteraTotemDTO(localizacao=Praça Central, descricao=Totem principal)";
            assertEquals(expected, dto.toString());
        }
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
    @DisplayName("Testes para NovaBicicletaDTO")
    class NovaBicicletaDTOTest {
        @Test
        public void testEqualsAndHashCode() {
            NovaBicicletaDTO n1 = new NovaBicicletaDTO();
            n1.setMarca("Caloi");
            n1.setModelo("Elite");
            n1.setAno(2023);

            NovaBicicletaDTO n2 = new NovaBicicletaDTO();
            n2.setMarca("Caloi");
            n2.setModelo("Elite");
            n2.setAno(2023);

            NovaBicicletaDTO n3 = new NovaBicicletaDTO();
            n3.setMarca("Monark");
            n3.setModelo("Classic");
            n3.setAno(2020);

            // n1 e n2 devem ser iguais
            assertEquals(n1, n2);
            assertEquals(n1.hashCode(), n2.hashCode());

            // n1 e n3 devem ser diferentes
            assertNotEquals(n1, n3);
            assertNotEquals(n1.hashCode(), n3.hashCode());
        }

        @Test
        public void testCanEqual() {
            NovaBicicletaDTO dto = new NovaBicicletaDTO();
            dto.setMarca("Caloi");
            dto.setModelo("Elite");
            dto.setAno(2023);

            assertTrue(dto.canEqual(new NovaBicicletaDTO()));
            assertFalse(dto.canEqual(new Object()));
        }

        @Test
        public void testToString() {
            NovaBicicletaDTO dto = new NovaBicicletaDTO();
            dto.setMarca("Caloi");
            dto.setModelo("Elite");
            dto.setAno(2023);

            String expected = "NovaBicicletaDTO(marca=Caloi, modelo=Elite, ano=2023)";
            assertEquals(expected, dto.toString());
        }
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
        public void testEqualsAndHashCode() {
            NovaTrancaDTO t1 = new NovaTrancaDTO();
            t1.setNumero("001");
            t1.setModelo("Modelo X");
            t1.setAnoDeFabricacao(2022);

            NovaTrancaDTO t2 = new NovaTrancaDTO();
            t2.setNumero("001");
            t2.setModelo("Modelo X");
            t2.setAnoDeFabricacao(2022);

            NovaTrancaDTO t3 = new NovaTrancaDTO();
            t3.setNumero("002");
            t3.setModelo("Modelo Y");
            t3.setAnoDeFabricacao(2020);

            // t1 e t2 devem ser iguais
            assertEquals(t1, t2);
            assertEquals(t1.hashCode(), t2.hashCode());

            // t1 e t3 devem ser diferentes
            assertNotEquals(t1, t3);
            assertNotEquals(t1.hashCode(), t3.hashCode());
        }

        @Test
        public void testCanEqual() {
            NovaTrancaDTO dto = new NovaTrancaDTO();
            dto.setNumero("001");
            dto.setModelo("Modelo X");
            dto.setAnoDeFabricacao(2022);

            assertTrue(dto.canEqual(new NovaTrancaDTO()));
            assertFalse(dto.canEqual(new Object()));
        }

        @Test
        public void testToString() {
            NovaTrancaDTO dto = new NovaTrancaDTO();
            dto.setNumero("001");
            dto.setModelo("Modelo X");
            dto.setAnoDeFabricacao(2022);

            String expected = "NovaTrancaDTO(numero=001, modelo=Modelo X, anoDeFabricacao=2022)";
            assertEquals(expected, dto.toString());
        }
        
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
        public void testEqualsAndHashCode() {
            NovoTotemDTO n1 = new NovoTotemDTO();
            n1.setLocalizacao("Praça Central");
            n1.setDescricao("Totem principal");

            NovoTotemDTO n2 = new NovoTotemDTO();
            n2.setLocalizacao("Praça Central");
            n2.setDescricao("Totem principal");

            NovoTotemDTO n3 = new NovoTotemDTO();
            n3.setLocalizacao("Rua 2");
            n3.setDescricao("Totem reserva");

            // n1 e n2 devem ser iguais
            assertEquals(n1, n2);
            assertEquals(n1.hashCode(), n2.hashCode());

            // n1 e n3 devem ser diferentes
            assertNotEquals(n1, n3);
            assertNotEquals(n1.hashCode(), n3.hashCode());
        }

        @Test
        public void testCanEqual() {
            NovoTotemDTO dto = new NovoTotemDTO();
            dto.setLocalizacao("Praça Central");
            dto.setDescricao("Totem principal");

            assertTrue(dto.canEqual(new NovoTotemDTO()));
            assertFalse(dto.canEqual(new Object()));
        }

        @Test
        public void testToString() {
            NovoTotemDTO dto = new NovoTotemDTO();
            dto.setLocalizacao("Praça Central");
            dto.setDescricao("Totem principal");

            String expected = "NovoTotemDTO(localizacao=Praça Central, descricao=Totem principal)";
            assertEquals(expected, dto.toString());
        }
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
    class TotemDTOTest {

        @Test
        public void testEqualsAndHashCode() {
            TotemDTO t1 = new TotemDTO();
            t1.setId(1L);
            t1.setLocalizacao("Praça Central");
            t1.setDescricao("Totem principal");

            TotemDTO t2 = new TotemDTO();
            t2.setId(1L);
            t2.setLocalizacao("Praça Central");
            t2.setDescricao("Totem principal");

            TotemDTO t3 = new TotemDTO();
            t3.setId(2L);
            t3.setLocalizacao("Rua 2");
            t3.setDescricao("Totem reserva");

            // t1 e t2 devem ser iguais
            assertEquals(t1, t2);
            assertEquals(t1.hashCode(), t2.hashCode());

            // t1 e t3 devem ser diferentes
            assertNotEquals(t1, t3);
            assertNotEquals(t1.hashCode(), t3.hashCode());
        }

        @Test
        public void testCanEqual() {
            TotemDTO totem = new TotemDTO();
            totem.setId(1L);
            totem.setLocalizacao("Centro");
            totem.setDescricao("Descrição");

            assertTrue(totem.canEqual(new TotemDTO()));
            assertFalse(totem.canEqual(new Object()));
        }

        @Test
        public void testToString() {
            TotemDTO totem = new TotemDTO();
            totem.setId(1L);
            totem.setLocalizacao("Centro");
            totem.setDescricao("Descrição");

            String expected = "TotemDTO(id=1, localizacao=Centro, descricao=Descrição)";
            assertEquals(expected, totem.toString());
        }
    }

    @Nested
    @DisplayName("Testes para TrancaIntegrarDTO")
    class TrancaIntegrarDTOTest {
        @Test
        public void testEqualsAndHashCode() {
            TrancaIntegrarDTO t1 = new TrancaIntegrarDTO(1L, 10L);
            TrancaIntegrarDTO t2 = new TrancaIntegrarDTO(1L, 10L);
            TrancaIntegrarDTO t3 = new TrancaIntegrarDTO(2L, 20L);

            // t1 e t2 devem ser iguais
            assertEquals(t1, t2);
            assertEquals(t1.hashCode(), t2.hashCode());

            // t1 e t3 devem ser diferentes
            assertNotEquals(t1, t3);
            assertNotEquals(t1.hashCode(), t3.hashCode());
        }

        @Test
        public void testCanEqual() {
            TrancaIntegrarDTO t = new TrancaIntegrarDTO(1L, 10L);

            assertTrue(t.canEqual(new TrancaIntegrarDTO()));
            assertFalse(t.canEqual(new Object()));
        }

        @Test
        public void testToString() {
            TrancaIntegrarDTO t = new TrancaIntegrarDTO(1L, 10L);

            String expected = "TrancaIntegrarDTO(idTotem=1, idFuncionario=10)";
            assertEquals(expected, t.toString());
        }
        @Test
        @DisplayName("Deve ter violação quando idTotem for nulo")
        void quandoIdTotemNulo_DeveTerViolacao() {
            TrancaIntegrarDTO dto = new TrancaIntegrarDTO();
            dto.setIdFuncionario(1l);
            dto.setIdTotem(null); // Valor inválido
            Set<ConstraintViolation<TrancaIntegrarDTO>> violations = validator.validate(dto);
            assertFalse(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes para TrancaRetirarDTO")
    class TrancaRetirarDTOTest {
        @Test
        public void testEqualsAndHashCode() {
            TrancaRetirarDTO t1 = new TrancaRetirarDTO();
            t1.setIdFuncionario(1L);
            t1.setStatusAcaoReparador("EM_REPARO");

            TrancaRetirarDTO t2 = new TrancaRetirarDTO();
            t2.setIdFuncionario(1L);
            t2.setStatusAcaoReparador("EM_REPARO");

            TrancaRetirarDTO t3 = new TrancaRetirarDTO();
            t3.setIdFuncionario(2L);
            t3.setStatusAcaoReparador("APOSENTADA");

            // t1 e t2 devem ser iguais
            assertEquals(t1, t2);
            assertEquals(t1.hashCode(), t2.hashCode());

            // t1 e t3 devem ser diferentes
            assertNotEquals(t1, t3);
            assertNotEquals(t1.hashCode(), t3.hashCode());
        }

        @Test
        public void testCanEqual() {
            TrancaRetirarDTO t = new TrancaRetirarDTO();
            t.setIdFuncionario(1L);
            t.setStatusAcaoReparador("EM_REPARO");

            assertTrue(t.canEqual(new TrancaRetirarDTO()));
            assertFalse(t.canEqual(new Object()));
        }

        @Test
        public void testToString() {
            TrancaRetirarDTO t = new TrancaRetirarDTO();
            t.setIdFuncionario(1L);
            t.setStatusAcaoReparador("EM_REPARO");

            String expected = "TrancaRetirarDTO(idFuncionario=1, statusAcaoReparador=EM_REPARO)";
            assertEquals(expected, t.toString());
        }
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
