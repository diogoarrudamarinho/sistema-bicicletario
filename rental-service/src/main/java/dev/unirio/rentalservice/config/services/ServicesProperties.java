package dev.unirio.rentalservice.config.services;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter @Setter
@ConfigurationProperties(prefix = "services.url")
public class ServicesProperties {
    
    private String equipmentservice;
    private String externalservice;
}
