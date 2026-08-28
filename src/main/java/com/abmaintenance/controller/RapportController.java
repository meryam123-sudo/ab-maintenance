package com.abmaintenance.controller;

import com.abmaintenance.repository.EquipementRepository;
import com.abmaintenance.repository.PanneRepository;
import com.abmaintenance.repository.InterventionRepository;
import com.abmaintenance.repository.MaintenanceRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RapportController {

    private final EquipementRepository equipementRepository;
    private final PanneRepository panneRepository;
    private final InterventionRepository interventionRepository;
    private final MaintenanceRepository maintenanceRepository;

    public RapportController(
            EquipementRepository equipementRepository,
            PanneRepository panneRepository,
            InterventionRepository interventionRepository,
            MaintenanceRepository maintenanceRepository) {

        this.equipementRepository = equipementRepository;
        this.panneRepository = panneRepository;
        this.interventionRepository = interventionRepository;
        this.maintenanceRepository = maintenanceRepository;
    }

    @GetMapping("/rapports")
    public String rapports(Model model) {

        model.addAttribute(
                "equipements",
                equipementRepository.findAll()
        );

        model.addAttribute(
                "pannes",
                panneRepository.findAll()
        );

        model.addAttribute(
                "interventions",
                interventionRepository.findAll()
        );

        model.addAttribute(
                "maintenances",
                maintenanceRepository.findAll()
        );

        model.addAttribute(
                "totalEquipements",
                equipementRepository.count()
        );

        model.addAttribute(
                "totalPannes",
                panneRepository.count()
        );

        model.addAttribute(
                "totalInterventions",
                interventionRepository.count()
        );

        model.addAttribute(
                "totalMaintenances",
                maintenanceRepository.count()
        );

        return "rapports/liste";
    }
}