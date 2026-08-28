package com.abmaintenance.service;

import com.abmaintenance.entity.Maintenance;
import com.abmaintenance.entity.StatutMaintenance;
import com.abmaintenance.repository.MaintenanceRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;

    public MaintenanceService(
            MaintenanceRepository maintenanceRepository) {

        this.maintenanceRepository = maintenanceRepository;
    }

    public List<Maintenance> findAll() {
        mettreAJourRetards();
        return maintenanceRepository.findAll();
    }
    public List<Maintenance> findByTechnicien(Long technicienId) {

    mettreAJourRetards();

    return maintenanceRepository
            .findByTechnicienId(technicienId);
}
    public List<Maintenance> findByEquipement(Long equipementId) {

    return maintenanceRepository
            .findByEquipementId(equipementId);
}

    public Maintenance findById(Long id) {

        return maintenanceRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Maintenance introuvable"
                        )
                );
    }

    public Maintenance save(Maintenance maintenance) {
        return maintenanceRepository.save(maintenance);
    }

    public void demarrer(Long id) {

        Maintenance maintenance = findById(id);

        maintenance.setStatut(
                StatutMaintenance.EN_COURS
        );

        maintenanceRepository.save(maintenance);
    }

    public void terminer(Long id) {

        Maintenance maintenance = findById(id);

        maintenance.setStatut(
                StatutMaintenance.TERMINEE
        );

        maintenance.setDateRealisation(
                LocalDate.now()
        );

        maintenanceRepository.save(maintenance);
    }

    public void mettreAJourRetards() {

        List<Maintenance> maintenances =
                maintenanceRepository
                        .findByDatePrevueBeforeAndStatut(
                                LocalDate.now(),
                                StatutMaintenance.PLANIFIEE
                        );

        for (Maintenance maintenance : maintenances) {

            maintenance.setStatut(
                    StatutMaintenance.EN_RETARD
            );

            maintenanceRepository.save(maintenance);
        }
    }
}