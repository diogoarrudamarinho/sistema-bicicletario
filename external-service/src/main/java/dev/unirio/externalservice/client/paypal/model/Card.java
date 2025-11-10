package dev.unirio.externalservice.client.paypal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Card {
    
    private String number;
    private String expiry;
    private String name;
    @JsonProperty("security_code")
    private String securityCode;
    
    public Card(String number, String expiry, String name, String securityCode) {
        this.number = number;
        this.expiry = expiry;
        this.name = name;
        this.securityCode = securityCode;
    }

    public String getNumber() {
        return number;
    }


    public void setNumber(String number) {
        this.number = number;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getsecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

}
