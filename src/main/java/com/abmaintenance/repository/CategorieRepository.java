package com.abmaintenance.repository;

import com.abmaintenance.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository
        extends JpaRepository<Categorie, Long> {
}