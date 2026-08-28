package com.abmaintenance.repository;

import com.abmaintenance.entity.Symptome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymptomeRepository
        extends JpaRepository<Symptome, Long> {
}