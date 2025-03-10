package it.itsincom.webdevd;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;

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
