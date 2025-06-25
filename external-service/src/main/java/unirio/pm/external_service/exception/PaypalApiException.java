package unirio.pm.external_service.exception;

import java.util.List;

public class PaypalApiException extends RuntimeException {
     private final int statusCode;
    private final String name;
    private final String debug_id;
    private final List<PaypalErrorDetail> details;

    public PaypalApiException(int statusCode, String name, String message, String debug_id, List<PaypalErrorDetail> details) {
        super(message);
        this.statusCode = statusCode;
        this.name = name;
        this.debug_id = debug_id;
        this.details = details;
    }

    public int getStatusCode() { return statusCode; }
    public String getName() { return name; }
    public String getDebug_id() { return debug_id; }
    public List<PaypalErrorDetail> getDetails() { return details; }

    public static class PaypalErrorDetail {
        private String issue;
        private String description;

        public String getIssue() { return issue; }
        public String getDescription() { return description; }
    }
}