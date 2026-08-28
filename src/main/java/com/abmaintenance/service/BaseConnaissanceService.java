package com.abmaintenance.service;

import com.abmaintenance.entity.Cause;
import com.abmaintenance.entity.Symptome;
import com.abmaintenance.repository.CauseRepository;
import com.abmaintenance.repository.SymptomeRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseConnaissanceService {

    private final SymptomeRepository symptomeRepository;
    private final CauseRepository causeRepository;

    public BaseConnaissanceService(
            SymptomeRepository symptomeRepository,
            CauseRepository causeRepository) {

        this.symptomeRepository = symptomeRepository;
        this.causeRepository = causeRepository;
    }

    public String construireContexte() {

        List<Symptome> symptomes =
                symptomeRepository.findAll();

        List<Cause> causes =
                causeRepository.findAll();

        StringBuilder contexte =
                new StringBuilder();

        contexte.append("""
                BASE DE CONNAISSANCES AB MAINTENANCE

                SYMPTOMES CONNUS :
                """);

        for (Symptome symptome : symptomes) {

            contexte.append("- ")
                    .append(symptome.getNom());

            if (symptome.getDescription() != null) {

                contexte.append(" : ")
                        .append(symptome.getDescription());
            }

            contexte.append("\n");
        }

        contexte.append("\nCAUSES CONNUES :\n");

        for (Cause cause : causes) {

            contexte.append("- ")
                    .append(cause.getNom());

            if (cause.getDescription() != null) {

                contexte.append(" : ")
                        .append(cause.getDescription());
            }

            if (cause.getSolution() != null) {

                contexte.append("\n  Solution recommandée : ")
                        .append(cause.getSolution());
            }

            contexte.append("\n");
        }

        return contexte.toString();
    }
}