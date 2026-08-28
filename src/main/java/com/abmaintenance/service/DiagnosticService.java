package com.abmaintenance.service;

import com.abmaintenance.entity.Cause;
import com.abmaintenance.entity.Panne;
import com.abmaintenance.entity.Symptome;
import com.abmaintenance.repository.CauseRepository;
import com.abmaintenance.repository.PanneRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DiagnosticService {

    private final PanneRepository panneRepository;
    private final CauseRepository causeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public DiagnosticService(
            PanneRepository panneRepository,
            CauseRepository causeRepository) {

        this.panneRepository = panneRepository;
        this.causeRepository = causeRepository;
    }

    public List<DiagnosticResultat> analyser(Long panneId) {

        Panne panne = panneRepository
                .findById(panneId)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Panne introuvable"
                    )
                );

        Map<Long, Double> scores = new HashMap<>();

        for (Symptome symptome : panne.getSymptomes()) {

            List<Object[]> resultats =
                    entityManager.createNativeQuery("""
                        SELECT id_cause, poids
                        FROM symptome_cause
                        WHERE id_symptome = :idSymptome
                    """)
                    .setParameter(
                        "idSymptome",
                        symptome.getId()
                    )
                    .getResultList();

            for (Object[] ligne : resultats) {

                Long idCause =
                        ((Number) ligne[0]).longValue();

                Double poids =
                        ((Number) ligne[1]).doubleValue();

                scores.merge(
                    idCause,
                    poids,
                    Double::sum
                );
            }
        }

        List<DiagnosticResultat> resultats =
                new ArrayList<>();

        for (Map.Entry<Long, Double> entry
                : scores.entrySet()) {

            Cause cause =
                    causeRepository
                        .findById(entry.getKey())
                        .orElseThrow();

            resultats.add(
                new DiagnosticResultat(
                    cause,
                    entry.getValue()
                )
            );
        }

        resultats.sort(
            Comparator.comparingDouble(
                DiagnosticResultat::getScore
            ).reversed()
        );

        return resultats;
    }
}