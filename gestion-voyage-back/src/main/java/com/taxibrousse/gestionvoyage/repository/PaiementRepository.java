package com.taxibrousse.gestionvoyage.repository;

import com.taxibrousse.gestionvoyage.model.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
}