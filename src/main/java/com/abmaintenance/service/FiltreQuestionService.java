package com.abmaintenance.service;

import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
public class FiltreQuestionService {

    private static final List<String> MOTS_MAINTENANCE = List.of(

            // Général
            "maintenance",
            "machine",
            "equipement",
            "equipements",
            "industriel",
            "industrielle",
            "panne",
            "diagnostic",
            "technicien",

            // Symptômes
            "bruit",
            "vibration",
            "surchauffe",
            "temperature",
            "fuite",
            "pression",
            "puissance",
            "odeur",
            "demarrage",
            "arrete",
            "arret",

            // Éléments mécaniques
            "pompe",
            "moteur",
            "roulement",
            "courroie",
            "filtre",
            "joint",
            "lubrification",
            "huile",
            "graisse",
            "alignement",
            "axe",
            "engrenage",

            // Électrique
            "electrique",
            "electrique",
            "tension",
            "courant",
            "cable",
            "fusible",
            "disjoncteur",
            "alimentation",

            // Pneumatique / hydraulique
            "hydraulique",
            "pneumatique",
            "compresseur",
            "verin",
            "flexible",
            "soupape"
    );

    public boolean estQuestionMaintenance(String question) {

        if (question == null || question.isBlank()) {
            return false;
        }

        String texte = normaliser(question);

        return MOTS_MAINTENANCE.stream()
                .anyMatch(texte::contains);
    }

    private String normaliser(String texte) {

        String resultat = Normalizer.normalize(
                texte.toLowerCase(),
                Normalizer.Form.NFD
        );

        return resultat.replaceAll(
                "\\p{M}",
                ""
        );
    }
}