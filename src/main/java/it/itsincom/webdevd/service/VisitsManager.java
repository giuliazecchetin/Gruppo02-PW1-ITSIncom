package it.itsincom.webdevd.service;


import it.itsincom.webdevd.model.Visit;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;

@ApplicationScoped
public class VisitsManager {
    private static final String CSV_FILE = "src/main/resources/data/visit.csv";



    public static void showAlert(String message) {
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(frame, message, "Attenzione", JOptionPane.WARNING_MESSAGE);
        frame.dispose();
    }


    public static List<Visit> getAllVisits() {
        List<Visit> visits = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                    String[] fields = line.split(",");
                    if (fields.length == 9) {
                        visits.add(new Visit(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6], fields[7], fields[8]));
                    }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return visits;
    }
    public static Visit getVisitById(String id) {
        String CSV_FILE = "src/main/resources/data/visit.csv";
        Visit visitsById = null;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                    String[] fields = line.split(",");
                    if (id.equals(fields[0])) {
                        visitsById = new Visit(fields[0], fields[1], fields[2], fields[3], fields[4],
                                fields[5], fields[6], fields[7], fields[8]);
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return visitsById;
    }

    public static Visit getVisitByDateAndHour(String date, String hour) {
        String CSV_FILE = "src/main/resources/data/visit.csv";
        Visit visitsByDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        hour = hour.trim();
        LocalTime startTime = LocalTime.parse(hour, DateTimeFormatter.ofPattern("HH:mm"));
        date = date.trim();
        LocalDate localDate = LocalDate.parse(date, formatter);
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 9) {
                    String dateField1 = fields[1];
                    dateField1 = dateField1.trim();
                    System.out.println("data1"+dateField1);
                    LocalDate localDate1 = LocalDate.parse(dateField1, formatter);
                    System.out.println("data1"+localDate1);
                    String timeString = fields[2];
                    timeString = timeString.trim();
                    LocalTime startTime1 = LocalTime.parse(hour, DateTimeFormatter.ofPattern("HH:mm"));
                    if (localDate.equals(localDate1) && startTime.equals(startTime1)) {
                        visitsByDate = new Visit(fields[0], fields[1], fields[2], fields[3], fields[4],
                                fields[5], fields[6], fields[7], fields[8]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
            return visitsByDate;
    }


    public static List<Visit> getVisitByFiscalCodeEmployee(String fiscalCode) {
        String CSV_FILE = "src/main/resources/data/visit.csv";
        fiscalCode = fiscalCode.trim();
        fiscalCode = fiscalCode.toUpperCase();
        List<Visit> visitsByEmployee = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                    String[] fields = line.split(",");
                    if(fields.length == 9) {
                        if (fiscalCode.equals(fields[7].trim())) {
                            visitsByEmployee.add(new Visit(fields[0], fields[1], fields[2], fields[3], fields[4],
                                    fields[5], fields[6], fields[7], fields[8]));
                        }
                    }
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
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter1);
        LocalDate today = LocalDate.now();
        LocalDate todayPlusDay = LocalDate.now().plusDays(1);
        visit.calculateDuration(visit.getStartTime(), visit.getEndTime());
        if (date.isEqual(todayPlusDay)|| date.isAfter(todayPlusDay)) {
            if (getVisitByDateAndHour(dateString, visit.getStartTime()) == null) {
                try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(CSV_FILE), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                    String dateToInsert = visit.getDate();
                    LocalDate localDate = LocalDate.parse(dateToInsert, formatter1);
                    String line = String.join(", ", visit.getId(),localDate.toString(), visit.getStartTime(), visit.getEndTime(),
                            visit.getDuration(), visit.getBadgeCode(), visit.getStatus(), visit.getFiscalCodeUser().toUpperCase(), visit.getFiscalCodeVisitor().toUpperCase());
                    bw.write(line);
                    bw.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (getVisitByDateAndHour(dateString, visit.getStartTime()) != null) {
                showAlert("Impossibile inserire la visita: esiste già per questa data e in questa ora!");
            }
        } else if (date.isBefore(todayPlusDay)) {
            showAlert("Impossibile inserire la visita: inserire la visita con un giorno d'anticipo!");
        }
    }


    // public static void removeVisit(Visit visit) {

        // try {
           // updatedLines = Files.lines(path)
                //    .filter(line -> !line.startsWith(fiscalCodeToDelete + ",")) // Assuming CSV is comma-separated
           //         .collect(Collectors.toList());

            // Write back the updated content
           // Files.write(path, updatedLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
         //   System.out.println("Deleted line with fiscalCode: " + fiscalCodeToDelete);
        // } catch (IOException e) {
        //    e.printStackTrace();
      //  }
    }
}

