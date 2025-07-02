package unirio.pm.external_service.client.paypal.model;

public class Card {
    
    private String number;
    private String expiry;
    private String name;
    private String security_code;
    
    public Card(String number, String expiry, String name, String security_code) {
        this.number = number;
        this.expiry = expiry;
        this.name = name;
        this.security_code = security_code;
    }

    public String getNumber() {
        return number;
    }


    public void setNumber(String number) {
        this.number = number;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getsecurityCode() {
        return security_code;
    }

    public void setSecurityCode(String security_code) {
        this.security_code = security_code;
    }

}
