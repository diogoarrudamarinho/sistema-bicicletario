package unirio.pm.external_service.exception;

public class ObjectNotFoundException extends RuntimeException {
    private final Long id;

    public ObjectNotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}