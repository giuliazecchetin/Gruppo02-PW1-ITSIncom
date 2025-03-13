package it.itsincom.webdevd.model;

public class Visitor {
    private String email;
    private String fiscalCode;
    private String nameSurname;
    private Number phoneNumber;

    public Visitor(String email, String fiscalCode, String nameSurname, String phoneNumber) {
        this.email = email;
        this.fiscalCode = fiscalCode;
        this.nameSurname = nameSurname;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public Number getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Number phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}
