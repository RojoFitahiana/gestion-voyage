package com.taxibrousse.gestionvoyage.repository;

import com.taxibrousse.gestionvoyage.model.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
}