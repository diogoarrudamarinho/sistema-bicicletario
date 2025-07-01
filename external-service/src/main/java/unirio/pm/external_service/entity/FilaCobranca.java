package unirio.pm.external_service.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_fila_cobranca")
public class FilaCobranca extends AbstractCobranca{
    
    public FilaCobranca() {
        super();
    }

    public FilaCobranca(BigDecimal valor, Long ciclista) {
        super(valor, ciclista);
    }
}

