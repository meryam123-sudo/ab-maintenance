package com.abmaintenance.controller;

import com.abmaintenance.entity.Equipement;

import com.abmaintenance.repository.CategorieRepository;
import com.abmaintenance.repository.SiteRepository;

import com.abmaintenance.service.EquipementService;
import com.abmaintenance.service.PanneService;
import com.abmaintenance.service.InterventionService;
import com.abmaintenance.service.MaintenanceService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/equipements")
public class EquipementController {

    private final EquipementService equipementService;

    private final CategorieRepository categorieRepository;

    private final SiteRepository siteRepository;

    private final PanneService panneService;

    private final InterventionService interventionService;

    private final MaintenanceService maintenanceService;


    public EquipementController(
            EquipementService equipementService,
            CategorieRepository categorieRepository,
            SiteRepository siteRepository,
            PanneService panneService,
            InterventionService interventionService,
            MaintenanceService maintenanceService) {

        this.equipementService =
                equipementService;

        this.categorieRepository =
                categorieRepository;

        this.siteRepository =
                siteRepository;

        this.panneService =
                panneService;

        this.interventionService =
                interventionService;

        this.maintenanceService =
                maintenanceService;
    }


    // ==========================================
    // LISTE DES ÉQUIPEMENTS
    // ==========================================

    @GetMapping
    public String liste(Model model) {

        model.addAttribute(
                "equipements",
                equipementService.findAll()
        );

        return "equipements/liste";
    }


    // ==========================================
    // AJOUTER UN ÉQUIPEMENT
    // ==========================================

    @GetMapping("/ajouter")
    public String ajouter(Model model) {

        model.addAttribute(
                "equipement",
                new Equipement()
        );

        model.addAttribute(
                "categories",
                categorieRepository.findAll()
        );

        model.addAttribute(
                "sites",
                siteRepository.findAll()
        );

        return "equipements/form";
    }


    // ==========================================
    // ENREGISTRER UN ÉQUIPEMENT
    // ==========================================

    @PostMapping("/enregistrer")
    public String enregistrer(
            @ModelAttribute Equipement equipement) {

        equipementService.save(equipement);

        return "redirect:/equipements";
    }


    // ==========================================
    // MODIFIER UN ÉQUIPEMENT
    // ==========================================

    @GetMapping("/modifier/{id}")
    public String modifier(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "equipement",
                equipementService.findById(id)
        );

        model.addAttribute(
                "categories",
                categorieRepository.findAll()
        );

        model.addAttribute(
                "sites",
                siteRepository.findAll()
        );

        return "equipements/form";
    }


    // ==========================================
    // SUPPRIMER UN ÉQUIPEMENT
    // ==========================================

    @GetMapping("/supprimer/{id}")
    public String supprimer(
            @PathVariable Long id) {

        equipementService.delete(id);

        return "redirect:/equipements";
    }


    // ==========================================
    // RÉGÉNÉRER TOUS LES QR CODES
    // ==========================================

    @GetMapping("/regenerer-qrcodes")
    public String regenererTousLesQRCodes() {

        equipementService.regenererTousLesQRCodes();

        return "redirect:/equipements";
    }


    // ==========================================
    // FICHE PUBLIQUE ACCESSIBLE PAR QR CODE
    // AUCUNE CONNEXION NÉCESSAIRE
    // ==========================================

    @GetMapping("/public/{id}")
    public String detailsPublics(
            @PathVariable Long id,
            Model model) {

        Equipement equipement =
                equipementService.findById(id);

        model.addAttribute(
                "equipement",
                equipement
        );

        model.addAttribute(
                "pannes",
                panneService.findByEquipement(id)
        );

        model.addAttribute(
                "interventions",
                interventionService.findByEquipement(id)
        );

        model.addAttribute(
                "maintenances",
                maintenanceService.findByEquipement(id)
        );

        return "equipements/public";
    }


    // ==========================================
    // FICHE INTERNE DE L'ÉQUIPEMENT
    // CONNEXION NÉCESSAIRE
    // ==========================================

    @GetMapping("/{id}")
    public String details(
            @PathVariable Long id,
            Model model) {

        Equipement equipement =
                equipementService.findById(id);

        model.addAttribute(
                "equipement",
                equipement
        );

        model.addAttribute(
                "pannes",
                panneService.findByEquipement(id)
        );

        model.addAttribute(
                "interventions",
                interventionService.findByEquipement(id)
        );

        model.addAttribute(
                "maintenances",
                maintenanceService.findByEquipement(id)
        );

        return "equipements/details";
    }
}