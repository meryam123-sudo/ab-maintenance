package com.abmaintenance.repository;

import com.abmaintenance.entity.Panne;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PanneRepository
        extends JpaRepository<Panne, Long> {

    List<Panne> findByEquipementIdOrderByDateSignalementDesc(
            Long equipementId
    );
}