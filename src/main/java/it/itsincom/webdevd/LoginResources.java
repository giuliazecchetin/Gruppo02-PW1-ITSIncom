package it.itsincom.webdevd;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/")
public class LoginResources {
    private final Template login;
    private final Template employee;

    public LoginResources(Template login, Template employee) {
        this.login = login;
        this.employee = employee;
    }

    @GET
    @Path("/login")
    public TemplateInstance getLoginPage() {
        return login.instance();
    }

    @POST
    @Path("/employee")
    public Response processLogin(@FormParam("email") String email, @FormParam("password") String password) {
        boolean isAuthenticated = true;

        if (isAuthenticated) {
            return Response.seeOther(URI.create("/employee")).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid credentials, please try again.")
                    .build();
        }
    }


}
