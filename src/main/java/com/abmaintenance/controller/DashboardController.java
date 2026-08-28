package com.abmaintenance.controller;

import com.abmaintenance.entity.EtatEquipement;
import com.abmaintenance.entity.StatutMaintenance;
import com.abmaintenance.repository.EquipementRepository;
import com.abmaintenance.repository.MaintenanceRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class DashboardController {

    private final EquipementRepository equipementRepository;
    private final MaintenanceRepository maintenanceRepository;

    public DashboardController(
            EquipementRepository equipementRepository,
            MaintenanceRepository maintenanceRepository) {

        this.equipementRepository = equipementRepository;
        this.maintenanceRepository = maintenanceRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            Model model,
            Authentication authentication) {

        // ==========================
        // STATISTIQUES
        // ==========================

        long totalEquipements =
                equipementRepository.count();

        long equipementsEnPanne =
                equipementRepository.countByEtat(
                        EtatEquipement.EN_PANNE
                );

        long maintenancesEnRetard =
                maintenanceRepository
                        .findByDatePrevueBeforeAndStatut(
                                LocalDate.now(),
                                StatutMaintenance.PLANIFIEE
                        )
                        .size();

        model.addAttribute(
                "totalEquipements",
                totalEquipements
        );

        model.addAttribute(
                "equipementsEnPanne",
                equipementsEnPanne
        );

        model.addAttribute(
                "maintenancesEnRetard",
                maintenancesEnRetard
        );


        // ==========================
        // ROLES
        // ==========================

        boolean isAdmin = false;
        boolean isResponsable = false;
        boolean isTechnicien = false;

        if (authentication != null) {

            isAdmin =
                    authentication.getAuthorities()
                            .stream()
                            .anyMatch(a ->
                                    "ROLE_ADMIN".equals(
                                            a.getAuthority()
                                    )
                            );

            isResponsable =
                    authentication.getAuthorities()
                            .stream()
                            .anyMatch(a ->
                                    "ROLE_RESPONSABLE".equals(
                                            a.getAuthority()
                                    )
                            );

            isTechnicien =
                    authentication.getAuthorities()
                            .stream()
                            .anyMatch(a ->
                                    "ROLE_TECHNICIEN".equals(
                                            a.getAuthority()
                                    )
                            );
        }

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

        return "dashboard";
    }
}