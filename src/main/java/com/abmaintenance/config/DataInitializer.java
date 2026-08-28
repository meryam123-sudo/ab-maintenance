package com.abmaintenance.config;

import com.abmaintenance.entity.Role;
import com.abmaintenance.entity.Utilisateur;
import com.abmaintenance.repository.RoleRepository;
import com.abmaintenance.repository.UtilisateurRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initialiser(
            RoleRepository roleRepository,
            UtilisateurRepository utilisateurRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (utilisateurRepository
                    .findByEmail("admin@abmaintenance.ma")
                    .isEmpty()) {

                Role adminRole =
                        roleRepository.findAll()
                                .stream()
                                .filter(r ->
                                    r.getNom().equals("ADMIN")
                                )
                                .findFirst()
                                .orElseThrow();

                Utilisateur admin = new Utilisateur();

                admin.setNom("Administrateur");
                admin.setPrenom("AB Maintenance");
                admin.setEmail(
                    "admin@abmaintenance.ma"
                );

                admin.setMotDePasse(
                    passwordEncoder.encode("Admin123!")
                );

                admin.setActif(true);
                admin.setRole(adminRole);

                utilisateurRepository.save(admin);
            }
        };
    }
}