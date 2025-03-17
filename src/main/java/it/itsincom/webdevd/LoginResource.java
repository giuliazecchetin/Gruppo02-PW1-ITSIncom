package it.itsincom.webdevd;

import io.quarkus.qute.Template;
import it.itsincom.webdevd.model.User;
import it.itsincom.webdevd.service.UsersManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import it.itsincom.webdevd.web.validation.UserValidator;
import it.itsincom.webdevd.service.CookiesSessionManager;
import java.io.FileNotFoundException;
import java.net.URI;

@Path("/login")
public class LoginResource {
    private final Template login;
    private final Template employee;
    private final Template reception;
    private final UserValidator userVer;
    private final CookiesSessionManager cookiesSessionManager;
    private final UsersManager usersManager;
    public LoginResource(Template login, Template employee, Template reception, UserValidator userVer, CookiesSessionManager cookiesSessionManager, UsersManager usersManager) {
        this.login = login;
        this.employee = employee;
        this.reception = reception;
        this.userVer = userVer;
        this.cookiesSessionManager = cookiesSessionManager;
        this.usersManager = usersManager;
    }

    @GET
    public Response getLoginPage(@CookieParam(CookiesSessionManager.COOKIE_SESSION) String sessionId) {
        // Remove the session if it exists
        if (sessionId != null && !sessionId.isEmpty()) {
            cookiesSessionManager.removeUserFromSession(sessionId);
        }

        // Create an expired cookie to clear the session cookie
        NewCookie expiredCookie = new NewCookie(
                CookiesSessionManager.COOKIE_SESSION, // Cookie name
                "",                           // Empty value
                "/",                          // Path (must match the original cookie path)
                null,                         // Domain (null for current domain)
                NewCookie.DEFAULT_VERSION,    // Version
                "",                           // Comment
                0,                            // Max age (0 to expire immediately)
                false                         // Secure flag (set to true if the original cookie was secure)
        );

        // Return the login page with the expired cookie
        return Response.ok(login.instance())
                .cookie(expiredCookie) // Set the expired cookie
                .build();
    }

    @POST
    public Response processLogin(@FormParam("email") String email, @FormParam("password") String password) throws FileNotFoundException {
        if (!userVer.checkAuthentification(email, password)) {
            return Response.status(401).entity("Invalid credentials").build();
        }
        User user = usersManager.getUserByEmail(email);
        // Create a new session cookie
        NewCookie sessionCookie = new NewCookie(
                CookiesSessionManager.COOKIE_SESSION, // Cookie name
                String.valueOf(cookiesSessionManager.createUserSession(user)), // Cookie value (session ID)
                "/",                          // Path (must match the path used to clear the cookie)
                null,                         // Domain (null for current domain)
                NewCookie.DEFAULT_VERSION,    // Version
                "",                           // Comment
                3600,                         // Max age in seconds (e.g., 1 hour)
                false                         // Secure flag (set to true for HTTPS)
        );

        // Redirect based on user role
        if ("dipendente".equals(userVer.checkUserRole(email, password))) {
            return Response.seeOther(URI.create("/employee")).cookie(sessionCookie).build();
        } else if ("portineria".equals(userVer.checkUserRole(email, password))) {
            return Response.seeOther(URI.create("/reception")).cookie(sessionCookie).build();
        }

        return Response.status(401).build();
    }
}
