package it.itsincom.webdevd;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

public class EmployeeResources {
    private final Template employee;

    public EmployeeResources(Template employee) {
        this.employee = employee;
    }

    @GET
    public TemplateInstance getEmployeePage() {
        return employee.instance();
    }
}
