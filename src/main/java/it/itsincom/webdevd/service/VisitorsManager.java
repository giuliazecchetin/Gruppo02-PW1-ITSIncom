package it.itsincom.webdevd.service;

import it.itsincom.webdevd.model.User;
import it.itsincom.webdevd.model.Visit;
import it.itsincom.webdevd.model.Visitor;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class VisitorsManager {
    private static final String CSV_FILE = "src/main/resources/data/visitorData.csv";
    private static final int ID_INDEX =4 ;

    public List<Visitor> getallVisitors() {
        return getallVisitors(new NoFilter(), false);

//        List<Visitor> visitors = new ArrayList<>();
//        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
//            br.readLine();
//            String line;
//            while ((line = br.readLine()) != null) {
//
//                    line.trim();
//                    String[] fields = line.split(",");
//                    if (fields.length == 6) {
//                        visitors.add(new Visitor(fields[0], fields[1], fields[2], fields[3]));
//                    }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return visitors;

    }
    private static List<Visitor> getallVisitors(Filter f, boolean breakOnFirstMatch) {
        List<Visitor> visitors = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                    line.trim();
                    String[] fields = line.split(",");
                    if (fields.length == 6) {
                        Visitor visitor = new Visitor(fields[ID_INDEX], fields[1], fields[2], fields[3]);
                        if (f.match(visitor)) {
                            visitors.add(visitor);
                            if (breakOnFirstMatch) {
                                break;
                            }
                        }
                    }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return visitors;

    }

    public static Visitor getallVisitorsByFiscalCode(String fiscalCode) {
        List<Visitor> visitors = getallVisitors(
                new FiscalCodeFilter(fiscalCode),
                true);
        return visitors.isEmpty() ? null : visitors.getFirst();

//        Visitor visitor = null;
//        int i = 0;
//        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                if (i != 0) {
//                    String[] fields = line.trim().split(",");
//                    if (fields.length == 6) {
//                       if (fields[1].equals(fiscalCode)) {
//                           visitor = new Visitor(fields[0], fields[1], fields[2], fields[3]);
//                       }
//                   }
//
//                }
//                i++;
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return visitor;

    }


    public static void addVisitor(Visitor visitor) {
        String fiscalCode = null;
        Visitor searchVisitor = null;
        fiscalCode = visitor.getFiscalCode();
        searchVisitor = getallVisitorsByFiscalCode(fiscalCode);
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(CSV_FILE), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

            if (searchVisitor == null){
                String line = String.join(",", visitor.getEmail(), visitor.getFiscalCode(), visitor.getNameSurname(), visitor.getPhoneNumber());
                bw.write(line);
                bw.newLine();
            } else {
                // TODO: handle when visitor already exist based on FC
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    interface Filter {
        boolean match(Visitor visitor);
    }

    static class NoFilter implements Filter {
        @Override
        public boolean match(Visitor visitor) {
            return true;
        }
    }

    static class FiscalCodeFilter implements Filter {
        private final String fiscalCode;

        public FiscalCodeFilter(String fiscalCode) {
            this.fiscalCode = fiscalCode;
        }
        @Override
        public boolean match(Visitor visitor) {
            return visitor.getFiscalCode().equals(fiscalCode);
        }
    }
}