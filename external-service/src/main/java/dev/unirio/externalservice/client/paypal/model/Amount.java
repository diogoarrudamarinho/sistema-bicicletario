package dev.unirio.externalservice.client.paypal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Amount {

    @JsonProperty("currency_code")
    private String currencyCode;
    private String value;

    public Amount(String currencyCode, String value) {
        this.currencyCode = currencyCode;
        this.value = value;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
