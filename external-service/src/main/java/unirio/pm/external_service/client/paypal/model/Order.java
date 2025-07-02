package unirio.pm.external_service.client.paypal.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String intent;
    private List<PurchaseUnit> purchase_units = new ArrayList<>();
    private PaymentSource payment_source;

    public Order(String intent, PaymentSource payment_source) {
        this.intent = intent;
        this.payment_source = payment_source;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public List<PurchaseUnit> getPurchase_units() {
        return purchase_units;
    }

    public void setPurchase_units(List<PurchaseUnit> purchase_units) {
        this.purchase_units = purchase_units;
    }

    public PaymentSource getPayment_source() {
        return payment_source;
    }

    public void setPayment_source(PaymentSource payment_source) {
        this.payment_source = payment_source;
    }


}
