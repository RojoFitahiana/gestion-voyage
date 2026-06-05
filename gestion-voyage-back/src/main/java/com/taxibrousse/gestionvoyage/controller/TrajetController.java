package com.taxibrousse.gestionvoyage.controller;

import com.taxibrousse.gestionvoyage.model.Trajet;
import com.taxibrousse.gestionvoyage.service.TrajetService;
import jakarta.persistence.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/trajets")
public class TrajetController {

    @Autowired
    private TrajetService trajetService;
    // Suppression de "private TrajetService trajetRepository;" car c'était une erreur

    // Récupérer tous les trajets (actifs et inactifs)
    @GetMapping("/all")

    public ResponseEntity<List<Trajet>> getAllTrajetsIncludingInactive() {
        List<Trajet> trajets = trajetService.getAllTrajets(); // Utilise le service, pas le repository
        return ResponseEntity.ok(trajets);
    }

    // Créer un nouveau trajet
    @PostMapping
    public ResponseEntity<Trajet> createTrajet(@RequestBody Trajet trajet) {
        Trajet createdTrajet = trajetService.createTrajet(trajet);
        return ResponseEntity.status(201).body(createdTrajet);
    }

    // Récupérer un trajet par ID
    @GetMapping("/{id}")
    public ResponseEntity<Trajet> getTrajetById(@PathVariable Long id) {
        Trajet trajet = trajetService.getTrajetById(id);
        return trajet != null ? ResponseEntity.ok(trajet) : ResponseEntity.notFound().build();
    }

    // Mettre à jour un trajet
    @PutMapping("/{id}")
    public ResponseEntity<Trajet> updateTrajet(@PathVariable Long id, @RequestBody Trajet trajet) {
        Trajet updatedTrajet = trajetService.updateTrajet(id, trajet);
        return updatedTrajet != null ? ResponseEntity.ok(updatedTrajet) : ResponseEntity.notFound().build();
    }

    // Supprimer un trajet
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrajet(@PathVariable Long id) {
        try {
            trajetService.deleteTrajet(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Désactiver un trajet
    @PutMapping("/{id}/desactiver")
    public ResponseEntity<Trajet> desactiverTrajet(@PathVariable Long id) {
        try {
            Trajet trajet = trajetService.desactiverTrajet(id);
            return ResponseEntity.ok(trajet);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Réactiver un trajet
    @PutMapping("/{id}/activer")
    public ResponseEntity<Trajet> activerTrajet(@PathVariable Long id) {
        try {
            Trajet trajet = trajetService.activerTrajet(id);
            return ResponseEntity.ok(trajet);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}