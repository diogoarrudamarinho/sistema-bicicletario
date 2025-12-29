package dev.unirio.equipmentservice.dto;

import dev.unirio.equipmentservice.enumeration.TrancaStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrancaIntegracaoDTO {
    
  private Long id;
  private Long bicicleta;
  private Integer numero;
  private String localizacao;
  private String anoDeFabricacao;
  private String modelo;
  private TrancaStatus status;


  public TrancaIntegracaoDTO(){
    // Construtor vazio
  }

  public TrancaIntegracaoDTO(Long id, Long bicicleta, Integer numero, 
    String localizacao, String anoDeFabricacao, String modelo, TrancaStatus status) {
    this.id = id;
    this.bicicleta = bicicleta;
    this.numero = numero;
    this.localizacao = localizacao;
    this.anoDeFabricacao = anoDeFabricacao;
    this.modelo = modelo;
    this.status = status;
  }

}
