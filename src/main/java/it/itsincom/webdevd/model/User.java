package it.itsincom.webdevd.model;

public class User {
    private String fiscalCode;
    private String nameSurname;
    private String email;
    private String password;
    private String phoneNumber;
    private String role ;


    public User(String fiscalCode, String nameSurname, String email, String password, String phoneNumber, String role) {
        this.fiscalCode = fiscalCode;
        this.nameSurname = nameSurname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;

    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    @Override
    public String toString() {
        return "User{" +
                "fiscalCode='" + fiscalCode + '\'' +
                ", nameSurname='" + nameSurname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
