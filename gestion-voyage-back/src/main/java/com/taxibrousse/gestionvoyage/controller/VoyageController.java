package com.taxibrousse.gestionvoyage.controller;

import com.taxibrousse.gestionvoyage.model.Voyage;
import com.taxibrousse.gestionvoyage.service.VoyageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/voyages")
public class VoyageController {

    @Autowired
    private VoyageService voyageService;

    // Récupérer tous les voyages (actifs et inactifs)
    @GetMapping("/all")
    public ResponseEntity<List<Voyage>> getAllVoyagesIncludingInactive() {
        List<Voyage> voyages = voyageService.getAllVoyages();
        return ResponseEntity.ok(voyages);
    }

    // Récupérer uniquement les voyages actifs
    @GetMapping
    public ResponseEntity<List<Voyage>> getAllActiveVoyages() {
        List<Voyage> voyages = voyageService.getAllActiveVoyages();
        return ResponseEntity.ok(voyages);
    }

    // Créer un nouveau voyage
    @PostMapping
    public ResponseEntity<Voyage> createVoyage(@RequestBody Voyage voyage) {
        voyage.setDateArrivee(null);
        Voyage createdVoyage = voyageService.createVoyage(voyage);
        return ResponseEntity.status(201).body(createdVoyage);
    }

    // Récupérer un voyage par ID
    @GetMapping("/{id}")
    public ResponseEntity<Voyage> getVoyageById(@PathVariable Long id) {
        return voyageService.getVoyageById(id)
                .map(voyage -> ResponseEntity.ok(voyage))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Mettre à jour un voyage
    @PutMapping("/{id}")
    public ResponseEntity<Voyage> updateVoyage(@PathVariable Long id, @RequestBody Voyage voyage) {
        Voyage updatedVoyage = voyageService.updateVoyage(id, voyage);
        return updatedVoyage != null ? ResponseEntity.ok(updatedVoyage) : ResponseEntity.notFound().build();
    }

    // Supprimer un voyage
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoyage(@PathVariable Long id) {
        try {
            voyageService.deleteVoyage(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Désactiver un voyage
    @PutMapping("/{id}/desactiver")
    public ResponseEntity<Voyage> desactiverVoyage(@PathVariable Long id) {
        try {
            Voyage voyage = voyageService.desactiverVoyage(id);
            return ResponseEntity.ok(voyage);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Réactiver un voyage
    @PutMapping("/{id}/activer")
    public ResponseEntity<Voyage> activerVoyage(@PathVariable Long id) {
        try {
            Voyage voyage = voyageService.activerVoyage(id);
            return ResponseEntity.ok(voyage);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Démarrer un voyage
    @PutMapping("/{id}/demarrer")
    public ResponseEntity<Voyage> demarrerVoyage(@PathVariable Long id) {
        try {
            Voyage voyage = voyageService.demarrerVoyage(id);
            return ResponseEntity.ok(voyage);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Terminer un voyage
    @PutMapping("/{id}/terminer")
    public ResponseEntity<Voyage> terminerVoyage(@PathVariable Long id) {
        try {
            Voyage voyage = voyageService.terminerVoyage(id);
            return ResponseEntity.ok(voyage);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}