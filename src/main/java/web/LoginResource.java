package web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import web.validation.UserValidator;

import java.io.FileNotFoundException;
import java.net.URI;

@Path("/login")
public class LoginResource {
    private final Template login;
    private final Template employee;
    private final UserValidator userVer;

    public LoginResource(Template login, Template employee, UserValidator userVer) {
        this.login = login;
        this.employee = employee;
        this.userVer = userVer;
    }

    @GET
    public TemplateInstance getLoginPage() {
        return login.instance();
    }

    @POST
    public Response processLogin(@FormParam("email") String email, @FormParam("password") String password) {
        try {
            if (userVer.checkAuthentification(email, password)) {
                if ("dipendente".equals(userVer.checkUserRole(email, password))) {
                    return Response.seeOther(URI.create("/employee")).build();
                } else if ("portineria".equals(userVer.checkUserRole(email, password))) {
                    return Response.status(401).build();
                    //Da creare ancora la pagina della portineria
                }
            }

        } catch (FileNotFoundException e) { throw new RuntimeException(e); }

        return Response.status(401).build();
    }
}
