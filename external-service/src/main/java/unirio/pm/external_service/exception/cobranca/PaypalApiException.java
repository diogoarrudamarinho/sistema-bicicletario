package unirio.pm.external_service.exception.cobranca;

import java.io.Serializable;
import java.util.List;

public class PaypalApiException extends RuntimeException {
    private int statusCode;
    private String name;
    private List<PaypalErrorDetail> details;

    public PaypalApiException(int statusCode, String name,  List<PaypalErrorDetail> details) {
        super();
        this.statusCode = statusCode;
        this.name = name;
        this.details = details;
    }

    public PaypalApiException(String message) {
        super(message);
    }

    public int getStatusCode() { return statusCode; }
    public String getName() { return name; }
    public List<PaypalErrorDetail> getDetails() { return details; }

    public static class PaypalErrorDetail implements Serializable{
        private String issue;
        private String description;

        public String getIssue() { return issue; }
        public String getDescription() { return description; }
    }
}