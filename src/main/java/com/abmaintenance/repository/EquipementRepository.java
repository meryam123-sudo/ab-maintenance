package com.abmaintenance.repository;

import com.abmaintenance.entity.Equipement;
import com.abmaintenance.entity.EtatEquipement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipementRepository
        extends JpaRepository<Equipement, Long> {

    Optional<Equipement> findByReference(String reference);

    long countByEtat(EtatEquipement etat);
}