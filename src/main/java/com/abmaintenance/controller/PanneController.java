package com.abmaintenance.controller;

import com.abmaintenance.entity.Panne;
import com.abmaintenance.entity.StatutPanne;
import com.abmaintenance.entity.Utilisateur;
import com.abmaintenance.repository.SymptomeRepository;
import com.abmaintenance.repository.UtilisateurRepository;
import com.abmaintenance.service.EquipementService;
import com.abmaintenance.service.PanneService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pannes")
public class PanneController {

    private final PanneService panneService;
    private final EquipementService equipementService;
    private final SymptomeRepository symptomeRepository;
    private final UtilisateurRepository utilisateurRepository;

    public PanneController(
            PanneService panneService,
            EquipementService equipementService,
            SymptomeRepository symptomeRepository,
            UtilisateurRepository utilisateurRepository) {

        this.panneService = panneService;
        this.equipementService = equipementService;
        this.symptomeRepository = symptomeRepository;
        this.utilisateurRepository = utilisateurRepository;
    }


    // ============================
    // LISTE DES PANNES
    // ============================

    @GetMapping
    public String liste(Model model) {

        model.addAttribute(
                "pannes",
                panneService.findAll()
        );

        return "pannes/liste";
    }


    // ============================
    // DECLARER UNE PANNE
    // ============================

    @GetMapping("/declarer/{equipementId}")
    public String declarer(
            @PathVariable Long equipementId,
            Model model) {

        Panne panne = new Panne();

        panne.setEquipement(
                equipementService.findById(equipementId)
        );

        model.addAttribute(
                "panne",
                panne
        );

        model.addAttribute(
                "symptomes",
                symptomeRepository.findAll()
        );

        return "pannes/form";
    }


    // ============================
    // ENREGISTRER UNE PANNE
    // ============================

    @PostMapping("/enregistrer")
    public String enregistrer(
            @ModelAttribute Panne panne,
            Authentication authentication) {

        Utilisateur utilisateur =
                utilisateurRepository
                        .findByEmail(
                                authentication.getName()
                        )
                        .orElseThrow();

        panne.setUtilisateur(
                utilisateur
        );

        panne.setStatut(
                StatutPanne.SIGNALEE
        );

        Panne saved =
                panneService.save(
                        panne
                );

        return "redirect:/pannes/" + saved.getId();
    }


    // ============================
    // DETAILS D'UNE PANNE
    // ============================

    @GetMapping("/{id}")
    public String details(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "panne",
                panneService.findById(id)
        );

        return "pannes/details";
    }


    // ============================
    // VALIDER UNE PANNE
    // ============================

    @PostMapping("/valider/{id}")
    public String valider(
            @PathVariable Long id) {

        Panne panne =
                panneService.findById(id);

        if (panne.getStatut()
                == StatutPanne.SIGNALEE) {

            panne.setStatut(
                    StatutPanne.VALIDEE
            );

            panneService.save(
                    panne
            );
        }

        return "redirect:/pannes";
    }

}