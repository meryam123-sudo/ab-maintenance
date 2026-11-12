package com.abmaintenance.repository;

import com.abmaintenance.entity.Panne;
import com.abmaintenance.entity.StatutPanne;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PanneRepository extends JpaRepository<Panne, Long> {

    // =====================================================
    // RÉCUPÉRER LES PANNES D'UN ÉQUIPEMENT
    // PLUS RÉCENTES EN PREMIER
    // =====================================================

    List<Panne> findByEquipementIdOrderByDateSignalementDesc(
            Long equipementId
    );

    // =====================================================
    // COMPTER LES PANNES PAR STATUT
    // =====================================================

    long countByStatut(StatutPanne statut);

    // =====================================================
    // SUPPRIMER LES PANNES D'UN ÉQUIPEMENT
    // =====================================================

    void deleteByEquipementId(Long equipementId);
}