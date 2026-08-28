package com.abmaintenance.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pannes")
public class Panne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_panne")
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "date_signalement", nullable = false)
    private LocalDateTime dateSignalement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UrgencePanne urgence = UrgencePanne.MOYENNE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPanne statut = StatutPanne.SIGNALEE;

    private String photo;

    @ManyToOne
    @JoinColumn(name = "id_equipement", nullable = false)
    private Equipement equipement;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateur utilisateur;

    @ManyToMany
    @JoinTable(
        name = "panne_symptome",
        joinColumns =
            @JoinColumn(name = "id_panne"),
        inverseJoinColumns =
            @JoinColumn(name = "id_symptome")
    )
    private Set<Symptome> symptomes = new HashSet<>();

    @PrePersist
    public void prePersist() {

        if (dateSignalement == null) {
            dateSignalement = LocalDateTime.now();
        }

        if (statut == null) {
            statut = StatutPanne.SIGNALEE;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateSignalement() {
        return dateSignalement;
    }

    public void setDateSignalement(LocalDateTime dateSignalement) {
        this.dateSignalement = dateSignalement;
    }

    public UrgencePanne getUrgence() {
        return urgence;
    }

    public void setUrgence(UrgencePanne urgence) {
        this.urgence = urgence;
    }

    public StatutPanne getStatut() {
        return statut;
    }

    public void setStatut(StatutPanne statut) {
        this.statut = statut;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Equipement getEquipement() {
        return equipement;
    }

    public void setEquipement(Equipement equipement) {
        this.equipement = equipement;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Set<Symptome> getSymptomes() {
        return symptomes;
    }

    public void setSymptomes(Set<Symptome> symptomes) {
        this.symptomes = symptomes;
    }
}