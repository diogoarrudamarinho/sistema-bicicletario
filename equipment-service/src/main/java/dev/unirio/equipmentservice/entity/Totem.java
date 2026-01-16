package dev.unirio.equipmentservice.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "totem")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Totem {
    
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String localizacao;

    @Column(nullable = true)
    private String descricao;

    @OneToMany(mappedBy = "totem")
    private List<Tranca> trancas;

    public Totem(){
        // Construtor vazio
    }
    
    public Totem(String localizacao, String descricao) {
        this.localizacao = localizacao;
        this.descricao = descricao;
    }
}
