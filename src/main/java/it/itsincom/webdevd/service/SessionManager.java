package it.itsincom.webdevd.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.NewCookie;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class SessionManager {

    public static final String COOKIE_SESSION = "Session";
    private final Map<String, String> sessions = new ConcurrentHashMap<>();

    public NewCookie createUserSession(String username) {
        String idSessione = UUID.randomUUID().toString();
        sessions.put(idSessione, username);
        return new NewCookie.Builder(COOKIE_SESSION)
                .value(idSessione)
                .build();
    }

    public String getUserFromSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public void removeUserFromSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
