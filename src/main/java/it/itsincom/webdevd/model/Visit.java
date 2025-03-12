package it.itsincom.webdevd.model;

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


    public Visit(String id, String date, String startTime, String endTime, String duration,
                 String badgeCode, String status, String fiscalCodeUser, String fiscalCodeVisitor) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.badgeCode = badgeCode;
        this.status = status;
        this.fiscalCodeUser = fiscalCodeUser;
        this.fiscalCodeVisitor = fiscalCodeVisitor;
    }

    public void calculateDuration() {
        try {
            String[] start = startTime.split(":");
            String[] end = endTime.split(":");
            int startMinutes = Integer.parseInt(start[0]) * 60 + Integer.parseInt(start[1]);
            int endMinutes = Integer.parseInt(end[0]) * 60 + Integer.parseInt(end[1]);
            this.duration = String.valueOf(endMinutes - startMinutes) + " min";
        } catch (Exception e) {
            this.duration = "0 min";
        }
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDate() { return date; }
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

