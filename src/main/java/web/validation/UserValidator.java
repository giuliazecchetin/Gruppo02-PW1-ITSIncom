package web.validation;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.*;

@ApplicationScoped
public class UserValidator {
    public boolean checkAuthentification(String email, String password) throws FileNotFoundException {
        File userLog = new File("src/main/resources/userLog.csv");
        FileReader userReader = new FileReader(userLog);
        boolean isValid = false;

        try (BufferedReader bf = new BufferedReader(new FileReader(userLog))) {
            String line;

            while ((line = bf.readLine()) != null) {
                String[] values = line.split(",");

                if (email.equals(values[0]) && password.equals(values[1])) {
                    return isValid = true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    public String checkUserRole(String email, String password) throws FileNotFoundException {
        File userLog = new File("src/main/resources/userLog.csv");
        FileReader userReader = new FileReader(userLog);
        String role = "";

        try (BufferedReader bf = new BufferedReader(new FileReader(userLog))) {
            String line;

            while ((line = bf.readLine()) != null) {
                String[] values = line.split(",");

                if (email.equals(values[0]) && password.equals(values[1])) {
                    if ("dipendente".equals(values[2])) {
                        return role = "dipendente";
                    } else if ("portineria".equals(values[2])) {
                        return role = "portineria";
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return role;
    }

}
