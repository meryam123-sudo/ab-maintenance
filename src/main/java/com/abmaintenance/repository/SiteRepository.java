package com.abmaintenance.repository;

import com.abmaintenance.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository
        extends JpaRepository<Site, Long> {
}