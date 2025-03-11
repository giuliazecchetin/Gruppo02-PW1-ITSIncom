package web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/employee")
public class EmployeeResource {
    private final Template employee;

    public EmployeeResource(Template employee) {
        this.employee = employee;
    }

    @GET
    public TemplateInstance getEmployeePage() {
        return employee.instance();
    }
}
