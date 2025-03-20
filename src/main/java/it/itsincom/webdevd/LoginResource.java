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

        if (sessionId != null && !sessionId.isEmpty()) {
            cookiesSessionManager.removeUserFromSession(sessionId);
        }


        NewCookie expiredCookie = new NewCookie(
                CookiesSessionManager.COOKIE_SESSION,
                "",
                "/",
                null,
                NewCookie.DEFAULT_VERSION,
                "",
                0,
                false
        );


        return Response.ok(login.data("message",null))
                .cookie(expiredCookie)
                .build();
    }

    @POST
    public Response processLogin(@FormParam("email") String email, @FormParam("password") String password) throws FileNotFoundException {
        if (!userVer.checkAuthentification(email, password)) {
            return Response.status(401).
                    entity(login.data("message","Credenziali invalide! Per favore, riprova.")).build();
        }
        User user = usersManager.getUserByEmail(email);

        NewCookie sessionCookie = new NewCookie(
                CookiesSessionManager.COOKIE_SESSION,
                String.valueOf(cookiesSessionManager.createUserSession(user)),
                "/",
                null,
                NewCookie.DEFAULT_VERSION,
                "",
                3600,
                false
        );


        if ("dipendente".equals(userVer.checkUserRole(email, password))) {
            return Response.seeOther(URI.create("/employee")).cookie(sessionCookie).build();
        } else if ("portineria".equals(userVer.checkUserRole(email, password))) {
            return Response.seeOther(URI.create("/reception")).cookie(sessionCookie).build();
        }

        return Response.status(401).build();
    }
}
