package it.itsincom.webdevd;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;

public class EmployeeResource {
    private final Template employee;
    private final Template login;

    public EmployeeResource(Template employee, Template login) {
        this.employee = employee;
        this.login = login;
    }

    @GET
    public TemplateInstance getEmployeePage() {
        return employee.instance();
    }
}
