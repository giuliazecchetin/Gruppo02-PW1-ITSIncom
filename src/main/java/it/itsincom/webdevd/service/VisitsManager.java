package it.itsincom.webdevd.service;


import it.itsincom.webdevd.model.Visit;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
                    String[] fields = line.split(",");
                    if (fields.length == 9) {
                        visits.add(new Visit(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6], fields[7], fields[8]));
                    }

                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return visits;
    }
    public static Visit getVisitById(String id) {
        String CSV_FILE = "src/main/resources/data/visit.csv";
        Visit visitsById = null;
        int i = 0;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (i != 0) {
                    String[] fields = line.split(",");
                    if (id.equals(fields[0])) {
                        visitsById = new Visit(fields[0], fields[1], fields[2], fields[3], fields[4],
                                fields[5], fields[6], fields[7], fields[8]);
                    }
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return visitsById;
    }

    public static List<Visit> getVisitByFiscalCodeEmployee(String fiscalCode) {
        String CSV_FILE = "src/main/resources/data/visit.csv";
        fiscalCode = fiscalCode.trim();
        fiscalCode = fiscalCode.toUpperCase();
        List<Visit> visitsByEmployee = new ArrayList<>();
        int i =0;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (i != 0) {
                    String[] fields = line.split(",");
                    if (fiscalCode.equals(fields[7].trim())) {
                        visitsByEmployee.add(new Visit(fields[0], fields[1], fields[2], fields[3], fields[4],
                                fields[5], fields[6], fields[7], fields[8]));
                    }
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return visitsByEmployee;
    }


    public static void addVisit(Visit visit) {
        String id1 = null;
        String id = null;
        do {
             id = visit.setId(UUID.randomUUID().toString());

        }while (getVisitById(id) != null);
        String dateString = visit.getDate();
        System.out.println(dateString);
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
        LocalDate today = LocalDate.now();
        LocalDate todayPlusDay = LocalDate.now().plusDays(1);
        visit.calculateDuration(visit.getStartTime(), visit.getEndTime());
        if (date.isEqual(todayPlusDay)|| date.isAfter(todayPlusDay)){
            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(CSV_FILE), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

                String line = String.join(", ", visit.getId(), visit.getDate(), visit.getStartTime(), visit.getEndTime(),
                        visit.getDuration(), visit.getBadgeCode(), visit.getStatus(), visit.getFiscalCodeUser(), visit.getFiscalCodeVisitor());
                bw.write(line);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
