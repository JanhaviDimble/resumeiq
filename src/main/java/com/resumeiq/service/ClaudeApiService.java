package com.resumeiq.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.*;

@Service
public class ClaudeApiService {

    

    @Value("${GROQ_API_KEY}")
private String apiKey;

    public String analyzeResume(String resumeText, String jobDescription) throws Exception {

        String prompt = "You are an expert resume analyzer. Analyze the following resume against the job description. Resume: "
                        + resumeText
                        + " Job Description: "
                        + jobDescription
                        + " Return a JSON response with: {matchScore: (0-100), qualityScore: (0-100), missingKeywords: [], suggestions: []}. Return ONLY JSON, no extra text.";

        String requestBody = "{"
                + "\"model\": \"llama-3.3-70b-versatile\","
                + "\"messages\": [{\"role\": \"user\", \"content\": \""
                + prompt.replace("\"", "\\\"").replace("\n", " ").replace("\r", " ")
                + "\"}]"
                + "}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("Groq Response: " + response.body());

        return response.body();
    }
}