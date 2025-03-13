package it.itsincom.webdevd.web.validation;

import io.quarkus.qute.Template;
import it.itsincom.webdevd.model.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.*;
import java.util.List;

import it.itsincom.webdevd.service.UsersManager;

@ApplicationScoped
public class UserValidator {
    private final UsersManager usersManager;

    public UserValidator(UsersManager usersManager) {
        this.usersManager = usersManager;
    }

    public boolean checkAuthentification(String email, String password) {
        List<User> users = usersManager.getallUsers();
        boolean isValid = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(email) && users.get(i).getPassword().equals(password)) {
                isValid = true;
            }
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
        List <User> users = usersManager.getallUsers();
        String role = "";
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(email) && users.get(i).getPassword().equals(password)) {
                if (users.get(i).getRole().equals("dipendente")) {
                    return role = "dipendente";
                }
                else if (users.get(i).getRole().equals("portineria")) {
                    return role = "portineria";
                }
            }
        }
        return role;
    }

}
