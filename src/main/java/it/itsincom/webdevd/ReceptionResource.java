package it.itsincom.webdevd;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.itsincom.webdevd.model.Visit;
import it.itsincom.webdevd.service.VisitsManager;
import it.itsincom.webdevd.web.validation.CredentialValidationErrors;
import it.itsincom.webdevd.web.validation.UserValidator;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.io.*;
import java.util.*;

@Path("/reception")
public class ReceptionResource {

    private final Template reception;
    private final VisitsManager visitsManager;
    static Map<String, String[]> visitList = new HashMap<>();
    static Map<String, String> userDataList = new HashMap<>();
    static Map<String, String> visitorDataList = new HashMap<>();

    public ReceptionResource(Template reception, VisitsManager visitsManager) {
        this.reception = reception;
        this.visitsManager = visitsManager;
    }



    @GET
    public TemplateInstance setReceptionResource() throws FileNotFoundException {
        List<Visit> visitsAll = VisitsManager.getAllVisits();

        return reception.data("visit", visitsAll);

    }


    public static void VisitReader() throws FileNotFoundException {
        File visit = new File("src/main/resources/data/visit.csv");
        File userData = new File("src/main/resources/data/userData.csv");
        File visitorData = new File("src/main/resources/data/visitorData.csv");



        //Employee
        try (BufferedReader bf = new BufferedReader(new FileReader(userData))) {
            String line;

            while ((line = bf.readLine()) != null) {
                String[] values = line.split(",");

                String key = values[1].trim();
                String value = values[2].trim();
                userDataList.put(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Visitor
        try (BufferedReader bf = new BufferedReader(new FileReader(visitorData))) {
            String line;

            while ((line = bf.readLine()) != null) {
                String[] values = line.split(",");

                String key = values[1].trim();
                String value = values[2].trim();
                visitorDataList.put(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Visit
        int i = 0;
        try (BufferedReader bf = new BufferedReader(new FileReader(visit))) {
            String line;

            while ((line = bf.readLine()) != null) {
                if (i == 0) {
                    i++;
                } else {
                    String[] values = line.split(",");

                    for(String x : userDataList.keySet()){
                        if(x.trim().equals(values[7].trim())){
                            visitList.put(values[0], values);
                            break;
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        visitList.forEach((key, value) -> System.out.println(key + " : " + Arrays.toString(value)));
        userDataList.forEach((key, value) -> System.out.println(key + " : " + value));
        visitorDataList.forEach((key, value) -> System.out.println(key + " : " + value));

    }
}
