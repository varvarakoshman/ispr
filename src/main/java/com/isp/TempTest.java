package com.isp;

import com.isp.dto.Cards;
import com.isp.dto.ChlamydiaPatient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.isp.dto.Request;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//TODO: перевести полностью принцип работы в контроллер
public class TempTest {
    public static void main(String[] args) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, ChlamydiaPatient> patients = new HashMap<>();

        // Using files to test
        try (FileReader reader = new FileReader("request.json");
             FileWriter questionWriter = new FileWriter("questionRequest.json");
             FileWriter responseWriter = new FileWriter("cardsResponse.json");
             FileReader responseReader = new FileReader("questionResponse.json")) {

            // Read the request
            Request request = gson.fromJson(reader, Request.class);
            // Extract patient
            ChlamydiaPatient patient = new ChlamydiaPatient(request.prefetch.patient);
            patients.put(patient.id, patient);

            // Check condition, based on cql tree
            // This condition is wrong. There is no pregnancy field in real CQL, there is diagnosticOrder.
            if (patient.pregnancy == -1) {
                // Create question request
                Request questionRequest = Request.questionRequest(request.prefetch.patient,
                        "Pregnancy", "Is the patient pregnant?", "boolean");
                // Send it (Change file writer to HTTP response)
                gson.toJson(questionRequest, questionWriter);

                // Wait

                // When new request appears, it is an answer
                Request answerRequest = gson.fromJson(responseReader, Request.class);
                // Fill the field
                patient.pregnancy = answerRequest.prefetch.questionnaireresponse.item[0].answer[0].valueBoolean ? 1 : 0;
            }

            // Create cards response
            Cards responseCard = Cards.cardsResponse("Screening required",
                    "Due to the bla-bla the screening is required.");
            // Send it (Change file writer to HTTP response)
            gson.toJson(responseCard, responseWriter);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
