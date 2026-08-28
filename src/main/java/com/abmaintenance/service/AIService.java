package com.abmaintenance.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class AIService {

    private final RestClient restClient;

    @Value("${ollama.model}")
    private String model;

    public AIService(
            @Value("${ollama.url}") String ollamaUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(ollamaUrl)
                .build();
    }

    public String genererExplication(String prompt) {

        Map<String, Object> request = Map.of(
                "model", model,
                "prompt", prompt,
                "stream", false
        );

        OllamaResponse response =
                restClient.post()
                        .uri("/api/generate")
                        .body(request)
                        .retrieve()
                        .body(OllamaResponse.class);

        if (response == null
                || response.response() == null) {

            return "L'IA n'a pas pu générer de recommandation.";
        }

        return response.response();
    }

    public record OllamaResponse(
            String response
    ) {
    }
}