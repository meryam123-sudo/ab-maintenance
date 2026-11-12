package com.abmaintenance.controller;

import com.abmaintenance.entity.EtatEquipement;
import com.abmaintenance.entity.StatutMaintenance;
import com.abmaintenance.entity.StatutPanne;
import com.abmaintenance.repository.EquipementRepository;
import com.abmaintenance.repository.MaintenanceRepository;
import com.abmaintenance.repository.PanneRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class DashboardController {

    private final EquipementRepository equipementRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final PanneRepository panneRepository;

    // =====================================================
    // CONSTRUCTEUR
    // =====================================================

    public DashboardController(
            EquipementRepository equipementRepository,
            MaintenanceRepository maintenanceRepository,
            PanneRepository panneRepository) {

        this.equipementRepository = equipementRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.panneRepository = panneRepository;
    }

    // =====================================================
    // DASHBOARD
    // =====================================================

    @GetMapping("/dashboard")
    public String dashboard(
            Model model,
            Authentication authentication) {

        // =================================================
        // STATISTIQUES
        // =================================================

        // Équipements
        long totalEquipements = equipementRepository.count();

        long equipementsEnPanne =
                equipementRepository.countByEtat(
                        EtatEquipement.EN_PANNE
                );

        // Pannes
        long totalPannes = panneRepository.count();

        long pannesSignalees =
                panneRepository.countByStatut(
                        StatutPanne.SIGNALEE
                );

        // Maintenances
        long totalMaintenances =
                maintenanceRepository.count();

        long maintenancesEnRetard =
                maintenanceRepository
                        .findByDatePrevueBeforeAndStatut(
                                LocalDate.now(),
                                StatutMaintenance.PLANIFIEE
                        )
                        .size();

        // =================================================
        // AJOUT DES STATISTIQUES AU MODEL
        // =================================================

        model.addAttribute(
                "totalEquipements",
                totalEquipements
        );

        model.addAttribute(
                "equipementsEnPanne",
                equipementsEnPanne
        );

        model.addAttribute(
                "totalPannes",
                totalPannes
        );

        model.addAttribute(
                "pannesSignalees",
                pannesSignalees
        );

        model.addAttribute(
                "totalMaintenances",
                totalMaintenances
        );

        model.addAttribute(
                "maintenancesEnRetard",
                maintenancesEnRetard
        );

        // =================================================
        // GESTION DES ROLES
        // =================================================

        boolean isAdmin = false;
        boolean isResponsable = false;
        boolean isTechnicien = false;

        if (authentication != null) {

            isAdmin = authentication
                    .getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            "ROLE_ADMIN".equals(
                                    authority.getAuthority()
                            )
                    );

            isResponsable = authentication
                    .getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            "ROLE_RESPONSABLE".equals(
                                    authority.getAuthority()
                            )
                    );

            isTechnicien = authentication
                    .getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            "ROLE_TECHNICIEN".equals(
                                    authority.getAuthority()
                            )
                    );
        }

        // =================================================
        // ROLES DANS LE MODEL
        // =================================================

        model.addAttribute(
                "isAdmin",
                isAdmin
        );

        model.addAttribute(
                "isResponsable",
                isResponsable
        );

        model.addAttribute(
                "isTechnicien",
                isTechnicien
        );

        // =================================================
        // RETOUR DU DASHBOARD
        // =================================================

        return "dashboard";
    }
}