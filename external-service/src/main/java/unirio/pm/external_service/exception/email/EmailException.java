package unirio.pm.external_service.exception.email;
public class EmailException extends RuntimeException {

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
