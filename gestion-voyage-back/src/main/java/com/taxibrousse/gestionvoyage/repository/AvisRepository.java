package com.taxibrousse.gestionvoyage.repository;

import com.taxibrousse.gestionvoyage.model.Avis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvisRepository extends JpaRepository<Avis, Long> {
    void deleteByVoyageId(Long voyageId);
}