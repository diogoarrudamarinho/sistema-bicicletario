package unirio.pm.external_service.exception.cobranca;

public class PaypalAuthException extends RuntimeException {
    
    public PaypalAuthException(String message) {
        super(message);
    }
}