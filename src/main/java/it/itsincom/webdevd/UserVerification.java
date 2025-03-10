package it.itsincom.webdevd;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.*;

@ApplicationScoped
public class UserVerification {
    public boolean checkAuthentification(String email, String password) throws FileNotFoundException {
        File userLog = new File("C:\\Users\\VladyslavBukator\\OneDrive - ITS Incom\\Documenti\\Lezioni ITSINCOM\\Java\\Gruppo02-PW1-ITSIncom\\src\\main\\resources\\userLog.csv");
        FileReader userReader = new FileReader(userLog);
        boolean isValid = false;

        try (BufferedReader bf = new BufferedReader(new FileReader(userLog))) {
            String line;

            while ((line = bf.readLine()) != null) {
                String[] values = line.split(",");

                if (email.equals(values[0]) && password.equals(values[1])) {
                    System.out.println("true");
                    return isValid = true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("false");
        return isValid;
    }

}
