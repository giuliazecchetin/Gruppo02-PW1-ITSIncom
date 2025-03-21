package it.itsincom.webdevd.service;

import io.quarkus.vertx.http.runtime.filters.accesslog.JBossLoggingAccessLogReceiver;
import it.itsincom.webdevd.model.Badge;
import it.itsincom.webdevd.model.Visit;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                    String badgeVisibility = fields[INDEX_BADGEVISIBILITY].trim();
                    boolean visible = false;
                    if(badgeVisibility.equals("true")){
                         visible= true;
                    }
                    if (visible) {
                        badges.add(new Badge(fields[INDEX_BADGECODE], Integer.parseInt(fields[INDEX_BADGENUMBER].trim()), Boolean.getBoolean(fields[INDEX_BADGEVISIBILITY].trim())));
                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("badges in badge manager" + badges);
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
                    if (id == Integer.parseInt(fields[INDEX_BADGENUMBER].trim())) {
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
    public static void deleteBadgeByCode(String codeBadge) {
        Path path = Paths.get(CSV_FILE);
        try {
            // Legge tutte le righe e filtra quelle che NON corrispondono all'ID da eliminare
            List<String> filteredLines = Files.lines(path)
                    .filter(line -> !line.startsWith(codeBadge.trim() + ",")) // Filtra la riga con l'ID specifico
                    .collect(Collectors.toList());

            // Riscrive il file con le righe rimanenti
            Files.write(path, filteredLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Visita con ID " + codeBadge + " eliminata con successo.");
        } catch (IOException e) {
            System.err.println("Errore durante l'eliminazione della visita: " + e.getMessage());
        }
    }
    public static void addBadge(Badge badge) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(CSV_FILE), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

            String line = String.join(",",
                    badge.getCodeBadge(),
                    String.valueOf(badge.getBadgeNumber()),
                    String.valueOf(badge.isBadgeVisible())
            );

            bw.write(line);
            bw.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
