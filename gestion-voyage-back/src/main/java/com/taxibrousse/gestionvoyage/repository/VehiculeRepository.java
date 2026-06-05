package com.taxibrousse.gestionvoyage.repository;

import com.taxibrousse.gestionvoyage.model.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
}