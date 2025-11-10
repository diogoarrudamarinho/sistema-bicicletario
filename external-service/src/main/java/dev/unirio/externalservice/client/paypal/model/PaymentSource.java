package dev.unirio.externalservice.client.paypal.model;

public class PaymentSource {

    private Card card;

    public PaymentSource(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
