package it.itsincom.webdevd;

import io.quarkus.qute.Template;
import it.itsincom.webdevd.model.User;
import it.itsincom.webdevd.model.Visit;
import it.itsincom.webdevd.model.Visitor;
import it.itsincom.webdevd.service.VisitorsManager;
import it.itsincom.webdevd.service.VisitsManager;
import it.itsincom.webdevd.service.CookiesSessionManager;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.*;

@Path("/employee")
public class EmployeeResource {
    private final Template employee;
    private final Template login;
    private final VisitsManager visitsManager;
    private static final String CSV_FILE = "visits.csv";
    private final CookiesSessionManager cookiesSessionManager;
    private final VisitorsManager visitorsManager;

    public EmployeeResource(Template employee, Template login, VisitsManager visitsManager, CookiesSessionManager cookiesSessionManager, VisitorsManager visitorsManager) {
        this.employee = employee;
        this.login = login;
        this.visitsManager = visitsManager;

        this.cookiesSessionManager = cookiesSessionManager;
        this.visitorsManager = visitorsManager;
    }


    @GET
    public Response getEmployeePage(@CookieParam(CookiesSessionManager.COOKIE_SESSION) String sessionId) {
        System.out.println("Session ID: " + sessionId);

        if (sessionId == null || sessionId.isEmpty()) {
            System.out.println("Redirecting to login page");

            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(login.data("message", "Unauthorized access. Please login.")
                            .data("redirect", true))
                    .build();
        }

        String fiscalCode = cookiesSessionManager.getUserFromSession(sessionId).getFiscalCode();
        System.out.println(fiscalCode);
        List<Visit> visits = VisitsManager.getVisitByFiscalCodeEmployee(fiscalCode);

        if (visits == null || visits.isEmpty()) {
            return Response.ok(employee.instance()).build();
        }

        visits.sort(Comparator.comparing(Visit::getDate).thenComparing(Visit::getStartTime).reversed());
        return Response.ok(employee.instance().data("visit", visits)).build();

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


    @POST
    @Path("/visits")
    public Response addVisit(@FormParam("email") String email, @FormParam("fiscalCode")String fiscalCode, @FormParam("nameSurname")String nameSurname, @FormParam("phoneNumber")String phoneNumber, @FormParam("dateVisit") String date, @FormParam("startTime") String startTime, @FormParam("endTime") String endTime, @CookieParam(CookiesSessionManager.COOKIE_SESSION) String sessionId) {
        Visitor visitor = new Visitor(email, fiscalCode, nameSurname, phoneNumber);
        VisitorsManager.addVisitor(visitor);
        String id = " ";
        String duration = "";
        String status = "NON INIZIATA";
        String fiscalCodeVisitor = visitor.getFiscalCode();
        String fiscalCodeUser = cookiesSessionManager.getUserFromSession(sessionId).getFiscalCode();
        String badge = "";
        Visit visit = new Visit(id, date, startTime, endTime, duration, badge,  status, fiscalCodeUser, fiscalCodeVisitor);
        VisitsManager.addVisit(visit);

        return Response.ok(employee.instance()).build();
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




