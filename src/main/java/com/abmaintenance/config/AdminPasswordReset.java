package com.abmaintenance.config;

import com.abmaintenance.repository.UtilisateurRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminPasswordReset implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminPasswordReset(
            UtilisateurRepository utilisateurRepository,
            PasswordEncoder passwordEncoder) {

        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        utilisateurRepository
                .findByEmail("admin@abmaintenance.ma")
                .ifPresent(admin -> {

                    admin.setMotDePasse(
                            passwordEncoder.encode("Admin123!")
                    );

                    admin.setActif(true);

                    utilisateurRepository.save(admin);

                    System.out.println(
                            "Mot de passe administrateur réinitialisé."
                    );
                });
    }
}