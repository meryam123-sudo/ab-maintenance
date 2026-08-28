package com.abmaintenance.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "diagnostics")
public class Diagnostic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_diagnostic")
    private Long id;

    @Column(name = "date_diagnostic", nullable = false)
    private LocalDateTime dateDiagnostic;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    @Column(columnDefinition = "TEXT")
    private String recommandation;

    @OneToOne
    @JoinColumn(name = "id_panne", nullable = false, unique = true)
    private Panne panne;

    @PrePersist
    public void prePersist() {
        if (dateDiagnostic == null) {
            dateDiagnostic = LocalDateTime.now();
        }
    }

    public Diagnostic() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateDiagnostic() {
        return dateDiagnostic;
    }

    public void setDateDiagnostic(LocalDateTime dateDiagnostic) {
        this.dateDiagnostic = dateDiagnostic;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getRecommandation() {
        return recommandation;
    }

    public void setRecommandation(String recommandation) {
        this.recommandation = recommandation;
    }

    public Panne getPanne() {
        return panne;
    }

    public void setPanne(Panne panne) {
        this.panne = panne;
    }
}