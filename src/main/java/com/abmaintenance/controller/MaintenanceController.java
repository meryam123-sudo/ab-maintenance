package com.abmaintenance.controller;

import com.abmaintenance.entity.Maintenance;
import com.abmaintenance.entity.StatutMaintenance;
import com.abmaintenance.repository.UtilisateurRepository;
import com.abmaintenance.service.EquipementService;
import com.abmaintenance.service.MaintenanceService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.abmaintenance.entity.Utilisateur;

import java.security.Principal;
@Controller
@RequestMapping("/maintenances")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;
    private final EquipementService equipementService;
    private final UtilisateurRepository utilisateurRepository;

    public MaintenanceController(
            MaintenanceService maintenanceService,
            EquipementService equipementService,
            UtilisateurRepository utilisateurRepository) {

        this.maintenanceService = maintenanceService;
        this.equipementService = equipementService;
        this.utilisateurRepository = utilisateurRepository;
    }

   @GetMapping
public String liste(
        Model model,
        Principal principal) {

    Utilisateur utilisateurConnecte =
            utilisateurRepository
                    .findByEmail(principal.getName())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Utilisateur connecté introuvable"
                            )
                    );

    if (utilisateurConnecte.getRole() != null
            && "TECHNICIEN".equals(
                    utilisateurConnecte
                            .getRole()
                            .getNom()
            )) {

        // Le technicien voit uniquement
        // ses maintenances
        model.addAttribute(
                "maintenances",
                maintenanceService
                        .findByTechnicien(
                                utilisateurConnecte.getId()
                        )
        );

        model.addAttribute(
                "modeTechnicien",
                true
        );

    } else {

        // ADMIN et RESPONSABLE
        // voient toutes les maintenances
        model.addAttribute(
                "maintenances",
                maintenanceService.findAll()
        );

        model.addAttribute(
                "modeTechnicien",
                false
        );
    }

    return "maintenances/liste";
}

    @GetMapping("/planifier/{equipementId}")
    public String planifier(
            @PathVariable Long equipementId,
            Model model) {

        Maintenance maintenance = new Maintenance();

        maintenance.setEquipement(
                equipementService.findById(equipementId)
        );

        model.addAttribute(
                "maintenance",
                maintenance
        );

        model.addAttribute(
        "techniciens",
        utilisateurRepository
                .findByRoleNomAndActif(
                        "TECHNICIEN",
                        true
                )
);

        return "maintenances/form";
    }

    @PostMapping("/enregistrer")
    public String enregistrer(
            @ModelAttribute Maintenance maintenance) {

        maintenance.setStatut(
                StatutMaintenance.PLANIFIEE
        );

        maintenanceService.save(maintenance);

        return "redirect:/maintenances";
    }

   @GetMapping("/demarrer/{id}")
public String demarrer(
        @PathVariable Long id,
        Principal principal) {

    Utilisateur utilisateurConnecte =
            utilisateurRepository
                    .findByEmail(principal.getName())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Utilisateur connecté introuvable"
                            )
                    );

    Maintenance maintenance =
            maintenanceService.findById(id);

    if ("TECHNICIEN".equals(
            utilisateurConnecte.getRole().getNom()
    )) {

        if (maintenance.getTechnicien() == null
                || !maintenance.getTechnicien()
                        .getId()
                        .equals(utilisateurConnecte.getId())) {

            throw new RuntimeException(
                    "Cette maintenance ne vous est pas affectée."
            );
        }
    }

    if (maintenance.getStatut()
            != StatutMaintenance.PLANIFIEE
            && maintenance.getStatut()
            != StatutMaintenance.EN_RETARD) {

        return "redirect:/maintenances";
    }

    maintenanceService.demarrer(id);

    return "redirect:/maintenances";
}

    @GetMapping("/terminer/{id}")
public String terminer(
        @PathVariable Long id,
        Principal principal) {

    Utilisateur utilisateurConnecte =
            utilisateurRepository
                    .findByEmail(principal.getName())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Utilisateur connecté introuvable"
                            )
                    );

    Maintenance maintenance =
            maintenanceService.findById(id);

    if ("TECHNICIEN".equals(
            utilisateurConnecte.getRole().getNom()
    )) {

        if (maintenance.getTechnicien() == null
                || !maintenance.getTechnicien()
                        .getId()
                        .equals(utilisateurConnecte.getId())) {

            throw new RuntimeException(
                    "Cette maintenance ne vous est pas affectée."
            );
        }
    }

    if (maintenance.getStatut()
            != StatutMaintenance.EN_COURS) {

        return "redirect:/maintenances";
    }

    maintenanceService.terminer(id);

    return "redirect:/maintenances";
}
}