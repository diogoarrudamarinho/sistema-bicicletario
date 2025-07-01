package unirio.pm.external_service.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_cobranca")
public class Cobranca extends AbstractCobranca{
   
    public Cobranca() {
        super();
    }

    public Cobranca(BigDecimal valor, Long ciclista) {
        super(valor, ciclista);
    }
}
