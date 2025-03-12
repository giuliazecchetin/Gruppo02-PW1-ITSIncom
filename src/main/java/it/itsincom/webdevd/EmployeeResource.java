package it.itsincom.webdevd;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.itsincom.webdevd.model.Visit;
import it.itsincom.webdevd.service.VisitsManager;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;

@Path("/employee")



public class EmployeeResource {
    private final Template employee;
    private final VisitsManager visitsManager;
    private static final String CSV_FILE = "visits.csv";

    public EmployeeResource(Template employee, VisitsManager visitsManager) {
        this.employee = employee;
        this.visitsManager = visitsManager;
    }


    @GET
    public TemplateInstance getEmployeePage() {
        return employee.instance();
    }



    @GET
     @Path("/sort")
    public Response sortVisit() {
        List<Visit> visits = VisitsManager.getAllVisits();
        if (visits == null || visits.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No visits found").build();
        }

        visits.sort(Comparator.comparing(Visit::getDate).thenComparing(Visit::getStartTime).reversed());
        return Response.ok(visits).build();
    }

    @GET
    @Path("/visits/{employeeId}")
    public Response getVisitsByEmployee(@PathParam("employeeId") String employeeId) {
        List<Visit> visits = (List<Visit>) VisitsManager.getVisitByIdEmployee(employeeId);
        if (visits == null || visits.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No visits found for employee ID: " + employeeId).build();
        }

        visits.sort(Comparator.comparing(Visit::getDate).thenComparing(Visit::getStartTime).reversed());
        return Response.ok(visits).build();
    }

    @POST
    @Path("/visits")
    public Response addVisit(Visit visit) {
        if (!isValidVisit(visit)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid visit data").build();
        }

        VisitsManager.addVisit(visit);
        return Response.ok(visit).build();
    }

    private boolean isValidVisit(Visit visit) {
        return visit != null &&
                visit.getId() != null &&
                visit.getDate() != null &&
                visit.getStartTime() != null &&
                visit.getEndTime() != null &&
                visit.getBadgeCode() != null &&
                visit.getStatus() != null &&
                visit.getFiscalCodeUser() != null &&
                visit.getFiscalCodeVisitor() != null;
    }
}




