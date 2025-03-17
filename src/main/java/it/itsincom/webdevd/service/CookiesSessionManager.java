package it.itsincom.webdevd.service;

import it.itsincom.webdevd.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.NewCookie;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class CookiesSessionManager {

    public static final String COOKIE_SESSION = "session";
    private final Map<String, User> sessions = new ConcurrentHashMap<>();

    public NewCookie createUserSession(User user) {
        String idSessione = UUID.randomUUID().toString();
        System.out.println(idSessione);
        System.out.println(user);
        sessions.put(idSessione, user);
        return new NewCookie.Builder(COOKIE_SESSION)
                .value(idSessione)
                .build();
    }

    public User getUserFromSession(String sessionId) {
        sessionId = sessionId.substring(sessionId.indexOf('=')+1);
        System.out.println(sessionId);
        User user = sessions.get(sessionId);
        System.out.println(user);
        return user;
    }

    public void removeUserFromSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
