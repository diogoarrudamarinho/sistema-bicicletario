package dev.unirio.rentalservice.entity;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "aluguel")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Aluguel {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private Long ciclista; 
    private Long bicicleta;
    private Long cobranca; 
    private Long trancaInicio;
    private Long trancaFim; 

    private LocalDateTime horaInicio;
    private LocalDateTime horaFim; 
    
    public Aluguel(Long ciclista, Long bicicleta, Long trancaInicio, Long cobranca) {
        this.ciclista = ciclista;
        this.cobranca = cobranca;
        this.bicicleta = bicicleta;
        this.trancaInicio = trancaInicio;
        this.horaInicio = LocalDateTime.now();
    }

    public boolean isAtivo() {
        return this.horaFim == null;
    }

    public boolean hasExcedente() {
        if (horaFim == null) 
            return false;
        
        return Duration.between(horaInicio, horaFim).toHours() > 2;
    }
    
    public BigDecimal calcularValorExcedente() {
        if (!hasExcedente()) 
            return BigDecimal.ZERO;

        Duration duracao = Duration.between(horaInicio, horaFim);
        Long minutosExcedentes = duracao.toMinutes() - 120;

        return new BigDecimal(5)
            .multiply(
                new BigDecimal(Math.ceil(minutosExcedentes / 30.0)));
    }
}