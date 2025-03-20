package it.itsincom.webdevd.service;

import it.itsincom.webdevd.model.User;
import it.itsincom.webdevd.model.Visit;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@ApplicationScoped
public class UsersManager {
    private static final String CSV_FILE = "src/main/resources/data/userData.csv";


    public List<User> getallUsers() {
        List<User> users = new ArrayList<>();
        int i = 0;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (i != 0) {
                    line.trim();
                    String[] fields = line.split(",");
                    if (fields.length == 6) {
                        users.add(new User(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]));
                    }

                }
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static List<User> getAllEmployees() {
        List<User> users = new ArrayList<>();
        int i = 0;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (i != 0) {
                    String[] fields = line.split(",");
                    if (fields.length == 6) {
                        String role = fields[5];
                        role = role.toLowerCase();
                        if (role.equals("dipendente") || role.equals("employee")) {
                            users.add(new User(fields[0], fields[1], fields[2],
                                    fields[3], fields[4], role));
                        }

                    }
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }


    public User getUserByEmail(String email) {
        User person = null;
        int i = 0;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (i != 0) {
                    String[] fields = line.split(",");
                    if (fields.length == 6) {
                        String code = fields[2];
                        code = code.toLowerCase();
                        code = code.trim();
                        if (code.equals(email)) {
                            person = new User(fields[0], fields[1], fields[2],
                                    fields[3], fields[4], fields[5]);
                        }

                    }
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return person;

    }




}


