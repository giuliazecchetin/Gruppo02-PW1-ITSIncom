package it.itsincom.webdevd;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.itsincom.webdevd.model.Badge;
import it.itsincom.webdevd.model.User;
import it.itsincom.webdevd.model.Visit;
import it.itsincom.webdevd.service.BadgesManager;
import it.itsincom.webdevd.service.CookiesSessionManager;
import it.itsincom.webdevd.service.UsersManager;
import it.itsincom.webdevd.service.VisitsManager;
import it.itsincom.webdevd.web.validation.CredentialValidationErrors;
import it.itsincom.webdevd.web.validation.UserValidator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import java.io.*;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Path("/reception")
public class ReceptionResource {
    private final Template reception;
    private final VisitsManager visitsManager;
    private final Template login;
    private final CookiesSessionManager cookiesSessionManager;
    private final UsersManager usersManager;
    private final BadgesManager badgesManager;

    public ReceptionResource(Template reception, VisitsManager visitsManager, Template login, CookiesSessionManager cookiesSessionManager, UsersManager usersManager, BadgesManager badgesManager) {
        this.reception = reception;
        this.visitsManager = visitsManager;
        this.login = login;
        this.cookiesSessionManager = cookiesSessionManager;
        this.usersManager = usersManager;
        this.badgesManager = badgesManager;
    }


    @GET
    public Response getReceptionPage(@CookieParam(CookiesSessionManager.COOKIE_SESSION) String sessionId) {
        System.out.println("Session ID: " + sessionId);
        List<Visit> visitsAll = VisitsManager.getAllVisits();
        if (sessionId == null || sessionId.isEmpty()) {
            System.out.println("Redirecting to login page");

            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(login.data("message", "Unauthorized access. Please login.")
                            .data("redirect", true))
                    .build();
        }

        String fiscalCode = cookiesSessionManager.getUserFromSession(sessionId).getFiscalCode();
        String nameUser = cookiesSessionManager.getUserFromSession(sessionId).getNameSurname();
        System.out.println(fiscalCode);
        List<Visit> visits = VisitsManager.getAllVisits();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        today.format(formatter);
        List<User> users = UsersManager.getAllEmployees();
        List<Badge> badges = new ArrayList<>();
        badges = BadgesManager.getAllBadges();
        System.out.println("Total badge: " + badges);
        if (visits == null || visits.isEmpty()) {
            return Response.ok(reception.instance()).build();
        }

        visits.sort(Comparator.comparing(Visit::getDate).thenComparing(Visit::getStartTime).reversed());
        return Response.ok(reception.data("visit", visits, "today", today, "nome", nameUser, "employees", users, "badge", badges)).build();
    }

    @GET
    @Path("/sort")
    public Response sortVisit(@QueryParam("dateSort") String dateSort, @CookieParam(CookiesSessionManager.COOKIE_SESSION) String sessionId) {
        LocalDate datePr;
        List<Visit> visits;
        List<User> users = UsersManager.getAllEmployees();
        visits = VisitsManager.getAllVisits();
        if (visits == null || visits.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No visits found").build();
        }
        LocalDate today = LocalDate.now();
        List<Badge> badges = new ArrayList<>();
        badges = BadgesManager.getAllBadges();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        today.format(formatter);
        String nameUser = cookiesSessionManager.getUserFromSession(sessionId).getNameSurname();
        if (dateSort == null) {
            visits.sort(Comparator.comparing(Visit::getDate).thenComparing(Visit::getStartTime).reversed());
            visits.removeLast();
            System.out.println(visits);
            return Response.ok(reception.data("visit", visits, "today", today, "nome", nameUser, "employees", users, "badge", badges)).build();
        }
        datePr = LocalDate.parse(dateSort);


        List<Visit> visitsWithSpecificDate = null;
        visitsWithSpecificDate = visits.stream().filter(v -> v.getLocalDate().isEqual(datePr)).toList();
        return Response.ok(reception.data("visit", visitsWithSpecificDate, "today", today, "nome", nameUser, "employees", users, "badge", badges)).build();
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

    @POST
    @Path("/badge")
    public Response badge(@CookieParam(CookiesSessionManager.COOKIE_SESSION) String sessionId, @FormParam("id") String id, @FormParam("badge") String badgeNum) {
        List<Visit> visitsAll = VisitsManager.getAllVisits();
        if (sessionId == null || sessionId.isEmpty()) {

            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(login.data("message", "Unauthorized access. Please login.")
                            .data("redirect", true))
                    .build();
        }
        String fiscalCode = cookiesSessionManager.getUserFromSession(sessionId).getFiscalCode();
        String nameUser = cookiesSessionManager.getUserFromSession(sessionId).getNameSurname();
        System.out.println(fiscalCode);
        List<Visit> visits = VisitsManager.getAllVisits();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        today.format(formatter);
        List<User> users = UsersManager.getAllEmployees();
        List<Badge> badges = new ArrayList<>();
        badges = BadgesManager.getAllBadges();
        if (visits == null || visits.isEmpty()) {
            return Response.ok(reception.instance()).build();
        }
        if (id != null) {
        Visit visitId = VisitsManager.getVisitById(id);

        if (visitId != null) {
            System.out.println("Visit: " + visitId);
            Badge badge1 = BadgesManager.getBadgeByBadgeNumber(Integer.parseInt(badgeNum.trim()));
            System.out.println("Badge: " + badge1);
            visitId.setBadgeCode(badge1.getCodeBadge().trim());
            visitId.setStatus("IN CORSO");
            VisitsManager.deleteVisitById(id);
            VisitsManager.addVisitWithoutControl(visitId);
            badge1.setBadgeVisible(false);
            BadgesManager.deleteBadgeByCode(badge1.getCodeBadge().trim());
            BadgesManager.addBadge(badge1);
        }
    }

        visits.sort(Comparator.comparing(Visit::getDate).thenComparing(Visit::getStartTime).reversed());
        return Response.ok(reception.data("visit", visits, "today", today, "nome", nameUser, "employees", users, "badge", badges)).build();
    }

    @POST
    @Path("/terminate")
    public Response terminateVisit(
            @CookieParam(CookiesSessionManager.COOKIE_SESSION) String sessionId,
            @FormParam("id") String id
    ) {
        if (sessionId == null || sessionId.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(login.data("message", "Unauthorized access. Please login.")
                            .data("redirect", true))
                    .build();
        }

        Visit visitId = VisitsManager.getVisitById(id);

        if (visitId != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            visitId.setStatus("TERMINATA");

            LocalTime hourNow = LocalTime.now();
            String hourNowStr = hourNow.format(formatter);
            hourNow = LocalTime.parse(hourNowStr.trim());
            if(hourNow.isBefore(LocalTime.parse(visitId.getStartTime().trim()))){
                LocalTime hourBefore = LocalTime.now();
                String hourBeforeStr = hourNow.format(formatter);
                hourBeforeStr = hourBeforeStr.trim();
                visitId.setStartTime(hourBeforeStr);
            }
            visitId.setEndTime(hourNow.toString());

            String badgeNumber = visitId.getBadgeCode().trim();


            Badge badge1 = BadgesManager.getBadgeByBadgeCode(badgeNumber);
            if (badge1 != null) {
                badge1.setBadgeVisible(true);
                BadgesManager.deleteBadgeByCode(badge1.getCodeBadge());
                BadgesManager.addBadge(badge1);
            }


            VisitsManager.deleteVisitById(id);
            VisitsManager.addVisitWithoutControl(visitId);
        }

        return Response.seeOther(URI.create("/reception")).build();

    }
    }





