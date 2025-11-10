package dev.unirio.externalservice.exception.cobranca;

public class PaypalAuthException extends RuntimeException {
    
    public PaypalAuthException(String message) {
        super(message);
    }
}