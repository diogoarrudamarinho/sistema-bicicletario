package unirio.pm.external_service.client.paypal.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {
    private String intent;

    @JsonProperty("purchase_units")
    private List<PurchaseUnit> purchaseUnits = new ArrayList<>();

    @JsonProperty("payment_source")
    private PaymentSource paymentSource;

    public Order(String intent, PaymentSource paymentSource) {
        this.intent = intent;
        this.paymentSource = paymentSource;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public List<PurchaseUnit> getPurchase_units() {
        return purchaseUnits;
    }

    public void setPurchase_units(List<PurchaseUnit> purchaseUnits) {
        this.purchaseUnits = purchaseUnits;
    }

    public PaymentSource getPayment_source() {
        return paymentSource;
    }

    public void setPayment_source(PaymentSource paymentSource) {
        this.paymentSource = paymentSource;
    }


}
