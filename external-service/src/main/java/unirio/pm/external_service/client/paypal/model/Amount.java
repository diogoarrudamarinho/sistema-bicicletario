package unirio.pm.external_service.client.paypal.model;

import java.math.BigDecimal;

public class Amount {

    private String currency_code;
    private BigDecimal value;

    public Amount(String currency_code, BigDecimal value) {
        this.currency_code = currency_code;
        this.value = value;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
