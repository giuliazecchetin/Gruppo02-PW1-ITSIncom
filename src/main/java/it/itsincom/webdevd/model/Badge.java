package it.itsincom.webdevd.model;

public class Badge {
    private String codeBadge;
    private int badgeNumber;
    private boolean badgeVisible;

    public Badge(String codeBadge, int badgeNumber, boolean badgeVisible) {
        this.codeBadge = codeBadge;
        this.badgeNumber = badgeNumber;
        this.badgeVisible = badgeVisible;
    }

    public String getCodeBadge() {
        return codeBadge;
    }

    public void setCodeBadge(String codeBadge) {
        this.codeBadge = codeBadge;
    }

    public int getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public boolean isBadgeVisible() {
        return badgeVisible;
    }

    public void setBadgeVisible(boolean badgeVisible) {
        this.badgeVisible = badgeVisible;
    }

    @Override
    public String toString() {
        return codeBadge + " " + badgeNumber;
    }
}
