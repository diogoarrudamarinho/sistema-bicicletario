// tests/validacao.test.js

const { validarCPF, validarEmail } = require('../services/validacao');

describe('Funções de validação', () => {
  describe('validarCPF', () => {
    it('deve retornar true para CPF válido (11 dígitos numéricos)', () => {
      expect(validarCPF('12345678901')).toBe(true);
    });

    it('deve retornar false para CPF com menos de 11 dígitos', () => {
      expect(validarCPF('1234567890')).toBe(false);
    });

    it('deve retornar false para CPF com letras', () => {
      expect(validarCPF('12345abc901')).toBe(false);
    });
  });

  describe('validarEmail', () => {
    it('deve retornar true para email válido', () => {
      expect(validarEmail('teste@exemplo.com')).toBe(true);
    });

    it('deve retornar false para email sem "@"', () => {
      expect(validarEmail('teste.exemplo.com')).toBe(false);
    });

    it('deve retornar false para email sem domínio', () => {
      expect(validarEmail('teste@')).toBe(false);
    });

    it('deve retornar false para string vazia', () => {
      expect(validarEmail('')).toBe(false);
    });
  });
});
