package it.itsincom.webdevd.service;

import it.itsincom.webdevd.model.User;
import it.itsincom.webdevd.model.Visitor;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class VisitorsManager {
    private static final String CSV_FILE = "src/main/resources/data/visitorData.csv";

    public List<Visitor> getallVisitors() {
        List<Visitor> visitors = new ArrayList<>();
        int i = 0;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (i != 0) {
                    line.trim();
                    String[] fields = line.split(",");
                    if (fields.length == 6) {
                        visitors.add(new Visitor(fields[0], fields[1], fields[2], fields[3].toString()));
                    }

                }
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return visitors;

    }

    public Visitor getallVisitorsByFiscalCode(String fiscalCode) {
        Visitor visitor = null;
        int i = 0;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (i != 0) {
                    line.trim();
                    String[] fields = line.split(",");
                   if (fields.length == 6) {
                       if (fields[1].equals(fiscalCode)) {
                           visitor = new Visitor(fields[0], fields[1], fields[2], fields[3].toString());
                       }
                   }

                }
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return visitor;

    }

}