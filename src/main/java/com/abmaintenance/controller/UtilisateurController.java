package com.abmaintenance.controller;

import com.abmaintenance.entity.Utilisateur;
import com.abmaintenance.repository.RoleRepository;
import com.abmaintenance.repository.UtilisateurRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurController(
            UtilisateurRepository utilisateurRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // ============================
    // LISTE
    // ============================

    @GetMapping
    public String liste(Model model) {

        model.addAttribute(
                "utilisateurs",
                utilisateurRepository.findAll()
        );

        return "utilisateurs/liste";
    }


    // ============================
    // AJOUTER
    // ============================

    @GetMapping("/ajouter")
    public String ajouter(Model model) {

        Utilisateur utilisateur = new Utilisateur();

        utilisateur.setActif(true);

        model.addAttribute(
                "utilisateur",
                utilisateur
        );

        model.addAttribute(
                "roles",
                roleRepository.findAll()
        );

        return "utilisateurs/form";
    }


    // ============================
    // MODIFIER
    // ============================

    @GetMapping("/modifier/{id}")
    public String modifier(
            @PathVariable Long id,
            Model model) {

        Utilisateur utilisateur =
                utilisateurRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Utilisateur introuvable : " + id
                                )
                        );

        // On ne renvoie jamais le hash BCrypt
        // dans le formulaire de modification.
        utilisateur.setMotDePasse("");

        model.addAttribute(
                "utilisateur",
                utilisateur
        );

        model.addAttribute(
                "roles",
                roleRepository.findAll()
        );

        return "utilisateurs/form";
    }


    // ============================
    // ENREGISTRER
    // ============================

    @PostMapping("/enregistrer")
    public String enregistrer(
            @ModelAttribute Utilisateur utilisateur) {


        // ============================
        // NOUVEL UTILISATEUR
        // ============================

        if (utilisateur.getId() == null) {


            // Le mot de passe est obligatoire
            // lors de la création d'un utilisateur.

            if (utilisateur.getMotDePasse() == null
                    || utilisateur.getMotDePasse().isBlank()) {

                throw new IllegalArgumentException(
                        "Le mot de passe est obligatoire"
                );
            }


            // Encodage BCrypt du mot de passe

            utilisateur.setMotDePasse(
                    passwordEncoder.encode(
                            utilisateur.getMotDePasse()
                    )
            );


            // Si la valeur actif n'arrive pas
            // depuis le formulaire, on active
            // l'utilisateur par défaut.

            if (utilisateur.getActif() == null) {

                utilisateur.setActif(true);
            }


            utilisateurRepository.save(utilisateur);

            return "redirect:/utilisateurs";
        }


        // ============================
        // MODIFICATION
        // ============================

        Utilisateur existant =
                utilisateurRepository.findById(
                        utilisateur.getId()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Utilisateur introuvable : "
                                        + utilisateur.getId()
                        )
                );


        // Mise à jour des informations

        existant.setNom(
                utilisateur.getNom()
        );

        existant.setPrenom(
                utilisateur.getPrenom()
        );

        existant.setEmail(
                utilisateur.getEmail()
        );

        existant.setTelephone(
                utilisateur.getTelephone()
        );

        existant.setRole(
                utilisateur.getRole()
        );


        // On évite de mettre actif à null.

        if (utilisateur.getActif() != null) {

            existant.setActif(
                    utilisateur.getActif()
            );
        }


        // ============================
        // MOT DE PASSE
        // ============================

        // Lors d'une modification,
        // le mot de passe est facultatif.
        //
        // S'il est vide, l'ancien mot de passe
        // BCrypt reste inchangé.

        if (utilisateur.getMotDePasse() != null
                && !utilisateur.getMotDePasse().isBlank()) {

            existant.setMotDePasse(
                    passwordEncoder.encode(
                            utilisateur.getMotDePasse()
                    )
            );
        }


        utilisateurRepository.save(existant);

        return "redirect:/utilisateurs";
    }


    // ============================
    // ACTIVER / DESACTIVER
    // ============================

    @GetMapping("/changer-statut/{id}")
    public String changerStatut(
            @PathVariable Long id) {

        Utilisateur utilisateur =
                utilisateurRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Utilisateur introuvable : " + id
                                )
                        );

        utilisateur.setActif(
                !Boolean.TRUE.equals(
                        utilisateur.getActif()
                )
        );

        utilisateurRepository.save(utilisateur);

        return "redirect:/utilisateurs";
    }


    // ============================
    // SUPPRIMER
    // ============================

    @GetMapping("/supprimer/{id}")
    public String supprimer(
            @PathVariable Long id) {

        if (!utilisateurRepository.existsById(id)) {

            throw new IllegalArgumentException(
                    "Utilisateur introuvable : " + id
            );
        }

        utilisateurRepository.deleteById(id);

        return "redirect:/utilisateurs";
    }
}