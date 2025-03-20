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
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            System.out.println("Reindirizzamento alla login page.");

            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(login.data("message", "Accesso non autorizzato. Per favore, effettua l'accesso.")
                            .data("redirect", true))
                    .build();
        }

        String fiscalCode = cookiesSessionManager.getUserFromSession(sessionId).getFiscalCode();
        String nameUser = cookiesSessionManager.getUserFromSession(sessionId).getNameSurname();
        System.out.println(fiscalCode);
        List<Visit> visits = VisitsManager.getVisitByFiscalCodeEmployee(fiscalCode);
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        today.format(formatter);

        if (visits == null || visits.isEmpty()) {
            return Response.ok(employee.instance()).build();
        }

        visits.sort(Comparator.comparing(Visit::getDate).thenComparing(Visit::getStartTime).reversed());
        return Response.ok(employee.data("visit", visits, "today", today, "nome", nameUser)).build();

    }

    @GET
     @Path("/sort")
    public Response sortVisit(@QueryParam("dateSort") String dateSort, @CookieParam(CookiesSessionManager.COOKIE_SESSION) String sessionId) {
        LocalDate datePr;
        List<Visit> visits;
        visits = VisitsManager.getAllVisits();
        if (visits == null || visits.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No visits found").build();
        }
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        today.format(formatter);
        String nameUser = cookiesSessionManager.getUserFromSession(sessionId).getNameSurname();
        if (dateSort == null){
            visits.sort(Comparator.comparing(Visit::getDate).thenComparing(Visit::getStartTime).reversed());
            visits.removeLast();
            System.out.println(visits);
            return Response.ok(employee.data("visit", visits , "today", today, "nome",nameUser)).build();
        }
        datePr = LocalDate.parse(dateSort);


        List <Visit> visitsWithSpecificDate = null;
        visitsWithSpecificDate = visits.stream().filter(v-> v.getLocalDate().isEqual(datePr)).toList();
        return Response.ok(employee.data("visit", visitsWithSpecificDate , "today", today, "nome",nameUser)).build();
    }


    @POST
    public Response addVisit(@FormParam("email") String email, @FormParam("fiscalCode")String fiscalCode, @FormParam("nameSurname")String nameSurname, @FormParam("phoneNumber")String phoneNumber, @FormParam("dateVisit") String date, @FormParam("startTime") String startTime, @FormParam("endTime") String endTime, @CookieParam(CookiesSessionManager.COOKIE_SESSION) String sessionId) {
        String id = " ";
        String duration = "";
        email = email.trim();
        email = email.replace(',', '.');
        fiscalCode = fiscalCode.trim();
        fiscalCode = fiscalCode.replace(',', '.');
        nameSurname = nameSurname.trim();
        nameSurname = nameSurname.replace(',', '.');
        phoneNumber = phoneNumber.trim();
        phoneNumber = phoneNumber.replace(',', '.');
        Visitor visitor = new Visitor(email, fiscalCode, nameSurname, phoneNumber);
        String status = "NON INIZIATA";
        String fiscalCodeVisitor = visitor.getFiscalCode();
        String fiscalCodeUser = cookiesSessionManager.getUserFromSession(sessionId).getFiscalCode();
        String badge = "NON ASSEGNATO";
        Visit newVisit = new Visit(id, date, startTime, endTime, duration, badge,  status, fiscalCodeUser, fiscalCodeVisitor);
        VisitsManager.addVisit(newVisit);
        VisitorsManager.addVisitor(visitor);

        return Response.seeOther(URI.create("/employee")).build();
    }


    @POST
    @Path("/logout")
    public Response logout(@CookieParam(CookiesSessionManager.COOKIE_SESSION) String SessionId) {
        if (SessionId != null) {
            cookiesSessionManager.removeUserFromSession(SessionId);
        }
        return Response.seeOther(URI.create("/login"))
                .cookie(new NewCookie(cookiesSessionManager.COOKIE_SESSION, "", "/login", null, "Session expired", 0, false))
                .build();
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




