package com.abmaintenance.repository;

import com.abmaintenance.entity.Intervention;
import com.abmaintenance.entity.StatutIntervention;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterventionRepository
        extends JpaRepository<Intervention, Long> {

    List<Intervention> findByPanneId(Long panneId);

    List<Intervention> findByTechnicienId(Long technicienId);

    List<Intervention> findByPanneEquipementId(Long equipementId);

    long countByStatut(StatutIntervention statut);


    // ==========================================
    // SUPPRIMER LES INTERVENTIONS
    // LIÉES AUX PANNES D'UN ÉQUIPEMENT
    // ==========================================

    void deleteByPanneEquipementId(Long equipementId);
}