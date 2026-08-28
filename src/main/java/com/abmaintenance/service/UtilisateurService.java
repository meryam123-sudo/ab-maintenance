package com.abmaintenance.service;

import com.abmaintenance.entity.Utilisateur;
import com.abmaintenance.repository.UtilisateurRepository;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class UtilisateurService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(
            UtilisateurRepository utilisateurRepository) {

        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Utilisateur utilisateur =
                utilisateurRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Utilisateur introuvable : " + email
                                )
                        );

        String role = utilisateur.getRole().getNom();

        if (role.startsWith("ROLE_")) {
            role = role.substring(5);
        }

        return User.builder()
                .username(utilisateur.getEmail())
                .password(utilisateur.getMotDePasse())
                .roles(role)
                .disabled(
                        !Boolean.TRUE.equals(
                                utilisateur.getActif()
                        )
                )
                .build();
    }
}