package com.abmaintenance.controller;

import com.abmaintenance.entity.Intervention;
import com.abmaintenance.entity.Panne;
import com.abmaintenance.entity.StatutIntervention;
import com.abmaintenance.entity.StatutPanne;
import com.abmaintenance.repository.UtilisateurRepository;
import com.abmaintenance.service.InterventionService;
import com.abmaintenance.service.PanneService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.abmaintenance.entity.Utilisateur;
import java.security.Principal;

@Controller
@RequestMapping("/interventions")
public class InterventionController {

    private final InterventionService interventionService;
    private final PanneService panneService;
    private final UtilisateurRepository utilisateurRepository;

    public InterventionController(
            InterventionService interventionService,
            PanneService panneService,
            UtilisateurRepository utilisateurRepository) {

        this.interventionService = interventionService;
        this.panneService = panneService;
        this.utilisateurRepository = utilisateurRepository;
    }

    // ===============================
    // LISTE DES INTERVENTIONS
    // ===============================

@GetMapping
public String liste(
        Model model,
        Principal principal) {

    // Récupérer l'utilisateur connecté grâce à son email
    Utilisateur utilisateurConnecte =
            utilisateurRepository
                    .findByEmail(principal.getName())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Utilisateur connecté introuvable"
                            )
                    );

    // Si c'est un TECHNICIEN :
    // afficher uniquement ses interventions
    if (utilisateurConnecte.getRole() != null
            && "TECHNICIEN".equals(
                    utilisateurConnecte.getRole().getNom()
            )) {

        model.addAttribute(
                "interventions",
                interventionService.findByTechnicien(
                        utilisateurConnecte.getId()
                )
        );

        model.addAttribute(
                "modeTechnicien",
                true
        );

    } else {

        // ADMIN et RESPONSABLE voient toutes les interventions
        model.addAttribute(
                "interventions",
                interventionService.findAll()
        );

        model.addAttribute(
                "modeTechnicien",
                false
        );
    }

    return "interventions/liste";
}

    // ===============================
    // CREER UNE INTERVENTION
    // ===============================

    @GetMapping("/creer/{panneId}")
public String creer(
        @PathVariable Long panneId,
        Model model) {

    Panne panne =
            panneService.findById(panneId);

    Intervention intervention =
            new Intervention();

    intervention.setPanne(panne);

    intervention.setStatut(
            StatutIntervention.PLANIFIEE
    );

    model.addAttribute(
            "intervention",
            intervention
    );

    model.addAttribute(
            "techniciens",
            utilisateurRepository
                    .findByRoleNomAndActif(
                            "TECHNICIEN",
                            true
                    )
    );

    return "interventions/form";
}

    // ===============================
    // ENREGISTRER UNE INTERVENTION
    // ===============================

    @PostMapping("/enregistrer")
    public String enregistrer(
            @ModelAttribute Intervention intervention) {

        Panne panne = panneService.findById(
                intervention.getPanne().getId()
        );

        intervention.setPanne(panne);

        if (intervention.getStatut() == null) {
            intervention.setStatut(
                    StatutIntervention.PLANIFIEE
            );
        }

        Intervention saved =
                interventionService.save(intervention);

        panne.setStatut(
                StatutPanne.EN_INTERVENTION
        );

        panneService.save(panne);

        return "redirect:/interventions/"
                + saved.getId();
    }

    // ===============================
    // DETAILS D'UNE INTERVENTION
    // ===============================

    @GetMapping("/{id}")
    public String details(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "intervention",
                interventionService.findById(id)
        );

        return "interventions/details";
    }

    // ===============================
    // DEMARRER UNE INTERVENTION
    // ===============================

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

    Intervention intervention =
            interventionService.findById(id);

    // Si c'est un technicien, il ne peut démarrer
    // que sa propre intervention
    if ("TECHNICIEN".equals(
            utilisateurConnecte.getRole().getNom()
    )) {

        if (intervention.getTechnicien() == null
                || !intervention.getTechnicien()
                        .getId()
                        .equals(utilisateurConnecte.getId())) {

            throw new RuntimeException(
                    "Cette intervention ne vous est pas affectée."
            );
        }
    }

    if (intervention.getStatut()
            != StatutIntervention.PLANIFIEE
            && intervention.getStatut()
            != StatutIntervention.AFFECTEE) {

        return "redirect:/interventions/" + id;
    }

    interventionService.demarrer(id);

    return "redirect:/interventions/" + id;
}

    // ===============================
    // TERMINER UNE INTERVENTION
    // ===============================

    @PostMapping("/terminer/{id}")
public String terminer(
        @PathVariable Long id,
        @ModelAttribute Intervention formulaire,
        Principal principal) {

    Utilisateur utilisateurConnecte =
            utilisateurRepository
                    .findByEmail(principal.getName())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Utilisateur connecté introuvable"
                            )
                    );

    Intervention intervention =
            interventionService.findById(id);

    // Le TECHNICIEN ne peut terminer
    // que l'intervention qui lui est affectée.
    if ("TECHNICIEN".equals(
            utilisateurConnecte.getRole().getNom()
    )) {

        if (intervention.getTechnicien() == null
                || !intervention.getTechnicien()
                        .getId()
                        .equals(utilisateurConnecte.getId())) {

            throw new RuntimeException(
                    "Cette intervention ne vous est pas affectée."
            );
        }
    }

    // On ne termine qu'une intervention en cours.
    if (intervention.getStatut()
            != StatutIntervention.EN_COURS) {

        return "redirect:/interventions/" + id;
    }

    intervention.setCauseReelle(
            formulaire.getCauseReelle()
    );

    intervention.setActionEffectuee(
            formulaire.getActionEffectuee()
    );

    intervention.setObservation(
            formulaire.getObservation()
    );

    intervention.setResultat(
            formulaire.getResultat()
    );

    interventionService.save(intervention);

    // Passe à TERMINEE + renseigne dateFin
    interventionService.terminer(id);

    intervention =
            interventionService.findById(id);

    // La panne associée devient résolue
    Panne panne =
            intervention.getPanne();

    panne.setStatut(
            StatutPanne.RESOLUE
    );

    panneService.save(panne);

    return "redirect:/interventions/" + id;
}}