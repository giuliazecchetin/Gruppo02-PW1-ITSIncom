package it.itsincom.webdevd.service;

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

    public static List<String> getAllBadges(){
        List<String> badges = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 2) {
                    badges.add(Integer.parseInt(fields[1]), fields[0]);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return badges;
    }
}
