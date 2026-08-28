package com.abmaintenance.repository;

import com.abmaintenance.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository
        extends JpaRepository<Client, Long> {
}