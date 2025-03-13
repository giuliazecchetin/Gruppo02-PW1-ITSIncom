package it.itsincom.webdevd.service;


import it.itsincom.webdevd.model.Visit;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class VisitsManager {
    private static final String CSV_FILE = "src/main/resources/data/visit.csv";

    public static List<Visit> getAllVisits() {
        List<Visit> visits = new ArrayList<>();
        int i =0;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (i != 0) {
                    String[] fields = line.split("','");
                    if (fields.length == 9) {
                        visits.add(new Visit(fields[0], fields[1], fields[2], fields[3], fields[4],
                                fields[5], fields[6], fields[7], fields[8]));
                    }

                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return visits;
    }


    public static Visit getVisitByIdEmployee(String id) {
        String CSV_FILE = "src/main/resources/data/visit.csv";
        List<Visit> visitsByEmployee = new ArrayList<>();
        return (Visit) visitsByEmployee;
    }


    public static void addVisit(Visit visit) {
        visit.setId(UUID.randomUUID().toString());
        visit.calculateDuration();
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(CSV_FILE), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

            String line = String.join("', '", visit.getId(), visit.getDate(), visit.getStartTime(), visit.getEndTime(),
                    visit.getDuration(), visit.getBadgeCode(), visit.getStatus(), visit.getFiscalCodeUser().replace("'", ""), visit.getFiscalCodeVisitor().replace("'", ""));
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
