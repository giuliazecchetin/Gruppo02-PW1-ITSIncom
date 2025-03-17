package it.itsincom.webdevd;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.itsincom.webdevd.model.Visit;
import it.itsincom.webdevd.service.CookiesSessionManager;
import it.itsincom.webdevd.service.VisitsManager;
import it.itsincom.webdevd.web.validation.CredentialValidationErrors;
import it.itsincom.webdevd.web.validation.UserValidator;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.io.*;
import java.util.*;

@Path("/reception")
public class ReceptionResource {
    private final Template reception;
    private final VisitsManager visitsManager;
    private final Template login;
    private final CookiesSessionManager cookiesSessionManager;

    public ReceptionResource(Template reception, VisitsManager visitsManager, Template login, CookiesSessionManager cookiesSessionManager) {
        this.reception = reception;
        this.visitsManager = visitsManager;
        this.login = login;
        this.cookiesSessionManager = cookiesSessionManager;
    }


    @GET
    public Response getReceptionPage(@CookieParam(CookiesSessionManager.COOKIE_SESSION) String sessionId) {
        System.out.println("Session ID: " + sessionId);
        List<Visit> visitsAll = VisitsManager.getAllVisits();
        if (sessionId == null || sessionId.isEmpty()) {
            System.out.println("Redirecting to login page");

            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(login.data("message", "Unauthorized access. Please login.")
                            .data("redirect", true))
                    .build();
        }

        System.out.println("Accessing Reception page");

        return Response.ok(reception.instance().data("visit", visitsAll)).build();
    }

}
