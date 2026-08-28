package com.abmaintenance.service;

import com.abmaintenance.entity.Panne;
import com.abmaintenance.repository.PanneRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PanneService {

    private final PanneRepository panneRepository;

    // Constructeur
    public PanneService(PanneRepository panneRepository) {
        this.panneRepository = panneRepository;
    }

    // Récupérer toutes les pannes
    public List<Panne> findAll() {
        return panneRepository.findAll();
    }

    // Récupérer une panne par son ID
    public Panne findById(Long id) {

        return panneRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Panne introuvable"
                        )
                );
    }

    // Récupérer les pannes d'un équipement
    // triées par date de signalement décroissante
    public List<Panne> findByEquipement(Long equipementId) {

        return panneRepository
                .findByEquipementIdOrderByDateSignalementDesc(
                        equipementId
                );
    }

    // Enregistrer une panne
    public Panne save(Panne panne) {

        // Générer automatiquement une référence
        // si aucune référence n'a été renseignée
        if (panne.getReference() == null
                || panne.getReference().isBlank()) {

            panne.setReference(
                    "PAN-" + System.currentTimeMillis()
            );
        }

        // Enregistrer la panne dans la base de données
        return panneRepository.save(panne);
    }
}