package com.abmaintenance.repository;

import com.abmaintenance.entity.Diagnostic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiagnosticRepository
        extends JpaRepository<Diagnostic, Long> {

    Optional<Diagnostic> findByPanneId(Long panneId);
}