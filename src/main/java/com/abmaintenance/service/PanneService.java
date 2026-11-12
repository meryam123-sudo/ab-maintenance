package com.abmaintenance.service;

import com.abmaintenance.entity.Equipement;
import com.abmaintenance.entity.EtatEquipement;
import com.abmaintenance.entity.Panne;
import com.abmaintenance.entity.StatutPanne;
import com.abmaintenance.repository.EquipementRepository;
import com.abmaintenance.repository.PanneRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PanneService {

    // =====================================================
    // REPOSITORIES
    // =====================================================

    private final PanneRepository panneRepository;
    private final EquipementRepository equipementRepository;

    // =====================================================
    // CONSTRUCTEUR
    // =====================================================

    public PanneService(
            PanneRepository panneRepository,
            EquipementRepository equipementRepository) {

        this.panneRepository = panneRepository;
        this.equipementRepository = equipementRepository;
    }

    // =====================================================
    // RÉCUPÉRER TOUTES LES PANNES
    // =====================================================

    public List<Panne> findAll() {
        return panneRepository.findAll();
    }

    // =====================================================
    // RÉCUPÉRER UNE PANNE PAR SON ID
    // =====================================================

    public Panne findById(Long id) {

        return panneRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Panne introuvable avec l'id : " + id
                        )
                );
    }

    // =====================================================
    // RÉCUPÉRER LES PANNES D'UN ÉQUIPEMENT
    // =====================================================

    public List<Panne> findByEquipement(Long equipementId) {

        return panneRepository
                .findByEquipementIdOrderByDateSignalementDesc(
                        equipementId
                );
    }

    // =====================================================
    // ENREGISTRER UNE PANNE
    // =====================================================

    @Transactional
    public Panne save(Panne panne) {

        // -------------------------------------------------
        // 1. GÉNÉRER LA RÉFÉRENCE
        // -------------------------------------------------

        if (panne.getReference() == null
                || panne.getReference().isBlank()) {

            panne.setReference(
                    "PAN-" + System.currentTimeMillis()
            );
        }

        // -------------------------------------------------
        // 2. DÉFINIR LA DATE DE SIGNALEMENT
        // -------------------------------------------------

        if (panne.getDateSignalement() == null) {

            panne.setDateSignalement(
                    LocalDateTime.now()
            );
        }

        // -------------------------------------------------
        // 3. DÉFINIR LE STATUT INITIAL
        // -------------------------------------------------

        if (panne.getStatut() == null) {

            panne.setStatut(
                    StatutPanne.SIGNALEE
            );
        }

        // -------------------------------------------------
        // 4. METTRE L'ÉQUIPEMENT EN PANNE
        // -------------------------------------------------

        if (panne.getEquipement() != null
                && panne.getEquipement().getId() != null) {

            Equipement equipement =
                    equipementRepository
                            .findById(
                                    panne.getEquipement().getId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Équipement introuvable"
                                    )
                            );

            equipement.setEtat(
                    EtatEquipement.EN_PANNE
            );

            equipementRepository.save(equipement);

            panne.setEquipement(equipement);
        }

        // -------------------------------------------------
        // 5. ENREGISTRER LA PANNE
        // -------------------------------------------------

        return panneRepository.save(panne);
    }

    // =====================================================
    // COMPTER LES PANNES SIGNALÉES
    // =====================================================

    public long countPannesSignalees() {

        return panneRepository.countByStatut(
                StatutPanne.SIGNALEE
        );
    }

    // =====================================================
    // COMPTER TOUTES LES PANNES
    // =====================================================

    public long count() {

        return panneRepository.count();
    }
}