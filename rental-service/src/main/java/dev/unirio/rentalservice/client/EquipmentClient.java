package dev.unirio.rentalservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import dev.unirio.rentalservice.dto.BicicletaDTO;
import dev.unirio.rentalservice.dto.TrancaDTO;
import dev.unirio.rentalservice.enumeration.BicicletaStatus;

@Component
public class EquipmentClient {
    
    private final WebClient client;

    public EquipmentClient(WebClient.Builder builder, @Value("${services.url.equipmentservice}") String baseUrl){
        client = builder.baseUrl(baseUrl).build();
    }

    public BicicletaDTO getBicicleta(Long id){
        return client.get()
                .uri("/biciclata/{id}", id)
                .retrieve()
                .bodyToMono(BicicletaDTO.class)
                .block();
    }

    public BicicletaDTO getBicicletaByTranca(Long id){
        return client.get()
                .uri("/{id}/bicicleta", id)
                .retrieve()
                .bodyToMono(BicicletaDTO.class)
                .block();
    }

    public BicicletaDTO postAlterarStatusBicicleta(Long id, BicicletaStatus status){
        return client.post()
            .uri("{id}/status/{acao}", id, status)
            .retrieve()
            .bodyToMono(BicicletaDTO.class)
            .block();
    }

    public TrancaDTO postDestrancar(Long trancaId, Long bicicletaId){
        return client.post()
            .uri("/{id}/destrancar/{bicicletaId}", trancaId, bicicletaId)
            .retrieve()
            .bodyToMono(TrancaDTO.class)
            .block();
    }
}
