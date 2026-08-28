package com.abmaintenance.controller;

import com.abmaintenance.service.AIService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AITestController {

    private final AIService aiService;

    public AITestController(AIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/test-ia")
    public String testerIA() {

        return aiService.genererExplication(
                """
                Tu es un assistant de maintenance industrielle.

                Explique brièvement les causes possibles
                d'une vibration excessive sur une pompe industrielle.

                Réponds en français, de manière concise et professionnelle.
                """
        );
    }
}