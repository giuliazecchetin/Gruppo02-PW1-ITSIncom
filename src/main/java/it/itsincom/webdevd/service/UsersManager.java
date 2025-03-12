package it.itsincom.webdevd.service;

import it.itsincom.webdevd.model.Visit;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@ApplicationScoped
public class UsersManager {
    private static final String CSV_FILE = "src/main/resources/data/userLog.csv";
    private static final String CSV_FILE2 = "src/main/resources/data/userData.csv";

}
