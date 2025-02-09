package com.dnd.chatty;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatbotController {

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";

    @PostMapping
    public String chat(@RequestBody String message) {
        try {
            String requestBody = "{ \"model\": \"llama3.2\", \"prompt\": \"" + message + "\", \"stream\": false }";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OLLAMA_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            return jsonResponse.optString("response", "No response key found");
        } catch (Exception e) {
            return "Error: Unable to connect to LLaMA 3.2.";
        }
    }
}
