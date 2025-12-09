package dev.unirio.equipmentservice.dto;

import dev.unirio.equipmentservice.enumeration.BicicletaStatus;

public class BicicletaDTO {
    
    private Long id;
    private String marca;
    private String modelo;
    private String ano;
    private Integer numero;
    private BicicletaStatus status;

    public BicicletaDTO(){
        // Construtor vazio
    }

    public BicicletaDTO(Long id, String marca, String modelo, String ano, 
                        Integer numero, BicicletaStatus status) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.numero = numero;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public BicicletaStatus getStatus() {
        return status;
    }

    public void setStatus(BicicletaStatus status) {
        this.status = status;
    }
}
