package com.abmaintenance.controller;

import com.abmaintenance.entity.Panne;
import com.abmaintenance.entity.Symptome;

import com.abmaintenance.repository.PanneRepository;
import com.abmaintenance.repository.SymptomeRepository;

import com.abmaintenance.service.DiagnosticService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/diagnostic")
public class DiagnosticController {

    private final DiagnosticService diagnosticService;
    private final PanneRepository panneRepository;
    private final SymptomeRepository symptomeRepository;

    public DiagnosticController(
            DiagnosticService diagnosticService,
            PanneRepository panneRepository,
            SymptomeRepository symptomeRepository) {

        this.diagnosticService = diagnosticService;
        this.panneRepository = panneRepository;
        this.symptomeRepository = symptomeRepository;
    }


    // ==============================
    // FORMULAIRE DES SYMPTÔMES
    // ==============================

    @GetMapping("/{panneId}")
    public String formulaire(
            @PathVariable Long panneId,
            Model model) {

        Panne panne =
                panneRepository
                        .findById(panneId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Panne introuvable"
                                )
                        );

        model.addAttribute(
                "panne",
                panne
        );

        model.addAttribute(
                "symptomes",
                symptomeRepository.findAll()
        );

        return "diagnostic/form";
    }


    // ==============================
    // ANALYSER LES SYMPTÔMES
    // ==============================

    @PostMapping("/analyser/{panneId}")
    public String analyser(
            @PathVariable Long panneId,
            @RequestParam(
                    name = "symptomeIds",
                    required = false
            )
            List<Long> symptomeIds,
            Model model) {

        Panne panne =
                panneRepository
                        .findById(panneId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Panne introuvable"
                                )
                        );


        // ==============================
        // RÉCUPÉRER LES SYMPTÔMES
        // ==============================

        Set<Symptome> symptomes =
                new HashSet<>();

        if (symptomeIds != null) {

            symptomes.addAll(
                    symptomeRepository
                            .findAllById(symptomeIds)
            );
        }


        // ==============================
        // ASSOCIER À LA PANNE
        // ==============================

        panne.setSymptomes(symptomes);

        panneRepository.save(panne);


        // ==============================
        // LANCER LE DIAGNOSTIC
        // ==============================

        model.addAttribute(
                "resultats",
                diagnosticService.analyser(panneId)
        );

        model.addAttribute(
                "panneId",
                panneId
        );

        model.addAttribute(
                "panne",
                panne
        );

        return "diagnostic/resultat";
    }
}