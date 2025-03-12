package it.itsincom.webdevd;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.itsincom.webdevd.model.Visit;
import it.itsincom.webdevd.service.VisitsManager;
import it.itsincom.webdevd.web.validation.CredentialValidationErrors;
import it.itsincom.webdevd.web.validation.UserValidator;
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

    public ReceptionResource(Template reception, VisitsManager visitsManager) {
        this.reception = reception;
        this.visitsManager = visitsManager;
    }

    @GET
    public TemplateInstance setReceptionResource() throws FileNotFoundException {
        List<Visit> visitsAll = VisitsManager.getAllVisits();

        return reception.data("visit", visitsAll);

    }
}
