package com.abmaintenance.repository;

import com.abmaintenance.entity.Utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository
        extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByEmail(String email);

    List<Utilisateur> findByRoleNomAndActif(
            String nomRole,
            Boolean actif
    );
}