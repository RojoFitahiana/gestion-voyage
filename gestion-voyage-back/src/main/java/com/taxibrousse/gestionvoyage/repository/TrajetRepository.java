package com.taxibrousse.gestionvoyage.repository;

import com.taxibrousse.gestionvoyage.model.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {
    List<Trajet> findByStatut(String statut);
}