package it.itsincom.webdevd.service;

import io.quarkus.vertx.http.runtime.filters.accesslog.JBossLoggingAccessLogReceiver;
import it.itsincom.webdevd.model.Badge;
import it.itsincom.webdevd.model.Visit;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class BadgesManager {
    private static final String CSV_FILE = "src/main/resources/data/badge.csv";
    private static final int INDEX_BADGECODE = 0;
    private static final int INDEX_BADGENUMBER = 1;
    private static final int INDEX_BADGEVISIBILITY = 2;


    public static List<Badge> getAllBadges(){
        List<Badge> badges = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 3) {
                    if(Boolean.getBoolean(fields[INDEX_BADGEVISIBILITY]))
                        badges.add(new Badge(fields[INDEX_BADGECODE], Integer.parseInt(fields[INDEX_BADGENUMBER]), Boolean.getBoolean(fields[INDEX_BADGEVISIBILITY])));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return badges;
    }


    public static Badge getBadgeByBadgeNumber(int id){
        String codeBadge = null;
        Badge badge = null;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 3) {
                    if (id == Integer.parseInt(fields[INDEX_BADGENUMBER])) {
                        badge = new Badge(fields[INDEX_BADGECODE], id, Boolean.getBoolean(fields[INDEX_BADGEVISIBILITY]));
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return badge;
    }

    public static Badge getBadgeByBadgeCode(String badgeCode){
        Badge badge = null;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 3) {
                    if (badgeCode.equals(fields[INDEX_BADGECODE])) {
                        badge = new Badge(badgeCode, Integer.parseInt(fields[INDEX_BADGENUMBER]), Boolean.getBoolean(fields[INDEX_BADGEVISIBILITY]));
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return badge;
    }


}
