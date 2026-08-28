package com.abmaintenance.service;

import com.abmaintenance.entity.Intervention;
import com.abmaintenance.entity.StatutIntervention;
import com.abmaintenance.repository.InterventionRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InterventionService {

    private final InterventionRepository interventionRepository;

    public InterventionService(
            InterventionRepository interventionRepository) {

        this.interventionRepository =
                interventionRepository;
    }

    // Récupérer toutes les interventions
    public List<Intervention> findAll() {
        return interventionRepository.findAll();
    }
    // Récupérer les interventions d'un technicien
public List<Intervention> findByTechnicien(Long technicienId) {

    return interventionRepository
            .findByTechnicienId(technicienId);
}

    // Récupérer une intervention par son ID
    public Intervention findById(Long id) {

        return interventionRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Intervention introuvable"
                        )
                );
    }

    // Récupérer les interventions d'un équipement
    public List<Intervention> findByEquipement(Long equipementId) {

        return interventionRepository
                .findByPanneEquipementId(equipementId);
    }

    // Enregistrer une intervention
    public Intervention save(
            Intervention intervention) {

        if (intervention.getReference() == null
                || intervention.getReference().isBlank()) {

            intervention.setReference(
                    "INT-" + System.currentTimeMillis()
            );
        }

        return interventionRepository.save(intervention);
    }

    // Démarrer une intervention
    public Intervention demarrer(Long id) {

        Intervention intervention = findById(id);

        intervention.setStatut(
                StatutIntervention.EN_COURS
        );

        intervention.setDateDebut(
                LocalDateTime.now()
        );

        return interventionRepository.save(intervention);
    }

    // Terminer une intervention
    public Intervention terminer(Long id) {

        Intervention intervention = findById(id);

        intervention.setStatut(
                StatutIntervention.TERMINEE
        );

        intervention.setDateFin(
                LocalDateTime.now()
        );

        return interventionRepository.save(intervention);
    }
}