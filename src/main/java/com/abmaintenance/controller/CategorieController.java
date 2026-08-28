package com.abmaintenance.controller;

import com.abmaintenance.entity.Categorie;
import com.abmaintenance.repository.CategorieRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategorieController {

    private final CategorieRepository categorieRepository;

    public CategorieController(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @GetMapping
    public String liste(Model model) {

        model.addAttribute(
                "categories",
                categorieRepository.findAll()
        );

        return "categories/liste";
    }

    @GetMapping("/ajouter")
    public String ajouter(Model model) {

        model.addAttribute(
                "categorie",
                new Categorie()
        );

        return "categories/form";
    }

    @GetMapping("/modifier/{id}")
    public String modifier(
            @PathVariable Long id,
            Model model) {

        Categorie categorie =
                categorieRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Catégorie introuvable : " + id
                                )
                        );

        model.addAttribute(
                "categorie",
                categorie
        );

        return "categories/form";
    }

    @PostMapping("/enregistrer")
    public String enregistrer(
            @ModelAttribute Categorie categorie) {

        categorieRepository.save(categorie);

        return "redirect:/categories";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(
            @PathVariable Long id) {

        categorieRepository.deleteById(id);

        return "redirect:/categories";
    }
}