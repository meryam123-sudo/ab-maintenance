package com.abmaintenance.config;

import com.abmaintenance.service.UtilisateurService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UtilisateurService utilisateurService;

    public SecurityConfig(
            UtilisateurService utilisateurService) {

        this.utilisateurService = utilisateurService;
    }

    // ===============================
    // ENCODAGE DES MOTS DE PASSE
    // ===============================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    // ===============================
    // FOURNISSEUR D'AUTHENTIFICATION
    // ===============================

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        utilisateurService
                );

        provider.setPasswordEncoder(
                passwordEncoder
        );

        return provider;
    }


    // ===============================
    // AUTHENTICATION MANAGER
    // ===============================

    @Bean
    public AuthenticationManager authenticationManager(
            DaoAuthenticationProvider authenticationProvider) {

        return new ProviderManager(
                authenticationProvider
        );
    }


    // ===============================
    // CONFIGURATION SECURITE
    // ===============================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager)
            throws Exception {

        http

            .authenticationManager(
                    authenticationManager
            )

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                    "/login",
                    "/css/**",
                    "/js/**",
                    "/images/**"
                )
                .permitAll()

                .requestMatchers(
                    "/admin/**",
                    "/utilisateurs/**"
                )
                .hasRole("ADMIN")

                .requestMatchers(
                    "/responsable/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "RESPONSABLE"
                )

                .requestMatchers(
                    "/technicien/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "RESPONSABLE",
                    "TECHNICIEN"
                )

                .anyRequest()
                .authenticated()
            )

            .formLogin(form -> form

                .loginPage("/login")

                .loginProcessingUrl("/login")

                .usernameParameter("username")

                .passwordParameter("password")

                .defaultSuccessUrl(
                        "/dashboard",
                        true
                )

                .failureUrl(
                        "/login?error=true"
                )

                .permitAll()
            )

            .logout(logout -> logout

                .logoutUrl("/logout")

                .logoutSuccessUrl(
                        "/login?logout=true"
                )

                .permitAll()
            );

        return http.build();
    }
}