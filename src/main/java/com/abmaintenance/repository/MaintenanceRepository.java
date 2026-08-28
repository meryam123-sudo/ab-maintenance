package com.abmaintenance.repository;

import com.abmaintenance.entity.Maintenance;
import com.abmaintenance.entity.StatutMaintenance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MaintenanceRepository
        extends JpaRepository<Maintenance, Long> {

    List<Maintenance> findByEquipementId(Long equipementId);

    List<Maintenance> findByTechnicienId(Long technicienId);

    List<Maintenance> findByDatePrevueBeforeAndStatut(
            LocalDate date,
            StatutMaintenance statut
    );
}