package dev.unirio.externalservice.exception.cobranca;

import java.io.Serializable;
import java.util.List;

public class PaypalApiException extends RuntimeException {
    private final int statusCode;
    private final String name;
    private final List<PaypalErrorDetail> details;

    public PaypalApiException(int statusCode, String name,  List<PaypalErrorDetail> details) {
        super();
        this.statusCode = statusCode;
        this.name = name;
        this.details = details;
    }

    public PaypalApiException(String message) {
        super(message);
        this.statusCode = 500;
        this.name = "INTERNAL_SERVER_ERROR";
        this.details = null;
    }

    public int getStatusCode() { return statusCode; }
    public String getName() { return name; }
    public List<PaypalErrorDetail> getDetails() { return details; }

    public static class PaypalErrorDetail implements Serializable{
        private final String issue;
        private final String description;

        public PaypalErrorDetail(String issue, String description) {
            this.issue = issue;
            this.description = description;
        }

        public String getIssue() { return issue; }
        public String getDescription() { return description; }
    }
}