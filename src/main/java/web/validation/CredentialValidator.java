package web.validation;

import io.quarkus.security.credential.Credential;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped

public class CredentialValidator {

    public CredentialValidationErrors validateEmail (String email) {
        boolean ok = email != null && !email.isEmpty();
        if (!ok) {
            return CredentialValidationErrors.EMPTY_EMAIL;
        }
        return null;
    }

    public CredentialValidationErrors validatePassword (String password) {
        boolean ok = password != null && !password.isEmpty();
        if (!ok) {
            return CredentialValidationErrors.EMPTY_PASSWORD;
        }
        return null;
    }
}
