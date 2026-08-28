package com.abmaintenance.controller;

import com.abmaintenance.entity.Role;
import com.abmaintenance.repository.RoleRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Liste des rôles
    @GetMapping
    public String liste(Model model) {

        model.addAttribute(
                "roles",
                roleRepository.findAll()
        );

        return "roles/liste";
    }

    // Formulaire d'ajout
    @GetMapping("/ajouter")
    public String ajouter(Model model) {

        model.addAttribute(
                "role",
                new Role()
        );

        return "roles/form";
    }

    // Formulaire de modification
    @GetMapping("/modifier/{id}")
    public String modifier(
            @PathVariable Long id,
            Model model) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Rôle introuvable : " + id
                        )
                );

        model.addAttribute(
                "role",
                role
        );

        return "roles/form";
    }

    // Enregistrer
    @PostMapping("/enregistrer")
    public String enregistrer(
            @ModelAttribute Role role) {

        roleRepository.save(role);

        return "redirect:/roles";
    }

    // Supprimer
    @GetMapping("/supprimer/{id}")
    public String supprimer(
            @PathVariable Long id) {

        roleRepository.deleteById(id);

        return "redirect:/roles";
    }
}