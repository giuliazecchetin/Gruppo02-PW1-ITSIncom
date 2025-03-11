package it.itsincom.webdevd.web.validation;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.*;

@ApplicationScoped
public class UserValidator {
    public boolean checkAuthentification(String email, String password) throws FileNotFoundException {
        File userLog = new File("src/main/resources/data/userLog.csv");
        FileReader userReader = new FileReader(userLog);
        boolean isValid = false;

        try (BufferedReader bf = new BufferedReader(new FileReader(userLog))) {
            String line;

            while ((line = bf.readLine()) != null) {
                String[] values = line.split(",");

                if (email.equals(values[0]) && password.equals(values[1])) {
                    return isValid = true;
                }
                
            }
            CredentialValidationErrors credentialValidationErrors = validateAccess(isValid);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    public CredentialValidationErrors validateAccess(boolean isValid){
        if (isValid!=true){
            return CredentialValidationErrors.INVALID_CREDENTIALS;
        }
        return null;
        
    }
    public String checkUserRole(String email, String password) throws FileNotFoundException {
        File userLog = new File("src/main/resources/data/userLog.csv");
        FileReader userReader = new FileReader(userLog);
        String role = "";

        try (BufferedReader bf = new BufferedReader(new FileReader(userLog))) {
            String line;

            while ((line = bf.readLine()) != null) {
                String[] values = line.split(",");

                if (email.equals(values[0]) && password.equals(values[1])) {
                    if ("dipendente".equals(values[2])) {
                        return role = "dipendente";
                    } else if ("portineria".equals(values[2])) {
                        return role = "portineria";
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return role;
    }

}
