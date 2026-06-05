package com.taxibrousse.gestionvoyage.repository;

import com.taxibrousse.gestionvoyage.model.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoyageRepository extends JpaRepository<Voyage, Long> {



    List<Voyage> findByStatut(String statut);

    // Méthode personnalisée pour récupérer les voyages par trajet_id
    List<Voyage> findByTrajetId(Long trajetId);

    // Méthode personnalisée pour supprimer les voyages par trajet_id
    void deleteByTrajetId(Long trajetId);

}