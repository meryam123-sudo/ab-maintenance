package com.abmaintenance.repository;

import com.abmaintenance.entity.Cause;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CauseRepository
        extends JpaRepository<Cause, Long> {
}