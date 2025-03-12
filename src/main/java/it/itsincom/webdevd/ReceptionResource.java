package it.itsincom.webdevd;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/reception")
public class ReceptionResource {

    private final Template reception;

    public ReceptionResource(Template reception) {
        this.reception = reception;
    }

    @GET
    public TemplateInstance getReceptionResource() {return reception.instance();}
}
