package it.itsincom.webdevd.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class Visit {
    private String id;
    private String date;
    private String startTime;
    private String endTime;
    private String duration;
    private String badgeCode;
    private String status;
    private String fiscalCodeUser;
    private String fiscalCodeVisitor;


    public Visit(String id, String date, String startTime, String endTime, String duration, String badgeCode,String status, String fiscalCodeUser, String fiscalCodeVisitor) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.badgeCode = badgeCode;
        this.fiscalCodeUser = fiscalCodeUser;
        this.fiscalCodeVisitor = fiscalCodeVisitor;
    }

    public void calculateDuration(String start, String end) {
        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = LocalTime.parse(end);
        if (endTime.isAfter(startTime)) {
            Duration duration = Duration.between(startTime, endTime);
            long hours = duration.toHours();
            this.duration = ""+hours;
        }
        else if (endTime.isBefore(startTime)) {
            this.endTime = "";
        }

    }

    // Getters e Setters
    public String getId() { return id; }
    public String setId(String id) { this.id = id;
        return id;
    }
    public String getDate() { return date; }


    public LocalDate getLocalDate() {
        String date1 = date.trim();
        return LocalDate.parse(date1);
    }
    public void setDate(String date) { this.date = date; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getDuration() { return duration; }
    public String getBadgeCode() { return badgeCode; }
    public void setBadgeCode(String badgeCode) { this.badgeCode = badgeCode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFiscalCodeUser() { return fiscalCodeUser; }
    public void setFiscalCodeUser(String fiscalCodeUser) { this.fiscalCodeUser = fiscalCodeUser; }
    public String getFiscalCodeVisitor() { return fiscalCodeVisitor; }
    public void setFiscalCodeVisitor(String fiscalCodeVisitor) { this.fiscalCodeVisitor = fiscalCodeVisitor; }


    @Override
    public String toString() {
        /*return  id + " " + date + " " + startTime + " " + endTime + " " +
                duration + " " + badgeCode + " " + status + " " + fiscalCodeUser +
                fiscalCodeVisitor;
        */
        return  startTime + "-" + endTime + " " +
                fiscalCodeVisitor + " " + fiscalCodeUser + " " +
                status + " " + badgeCode;
    }
}

