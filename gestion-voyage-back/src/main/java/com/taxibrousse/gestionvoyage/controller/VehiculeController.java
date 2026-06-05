package com.taxibrousse.gestionvoyage.controller;

import com.taxibrousse.gestionvoyage.model.Vehicule;
import com.taxibrousse.gestionvoyage.service.VehiculeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicules")
@CrossOrigin(origins = "http://localhost:4200")
public class VehiculeController {

    @Autowired
    private VehiculeService vehiculeService;

    // Récupérer tous les véhicules
    @GetMapping
    public List<Vehicule> getAllVehicules() {
        return vehiculeService.getAllVehicules();
    }

    // Créer un nouveau véhicule
    @PostMapping
    public Vehicule createVehicule(@RequestBody Vehicule vehicule) {
        return vehiculeService.createVehicule(vehicule);
    }

    // Mettre à jour un véhicule existant
    @PutMapping("/{id}")
    public Vehicule updateVehicule(@PathVariable Long id, @RequestBody Vehicule vehicule) {
        return vehiculeService.updateVehicule(id, vehicule);
    }

    // Récupérer un véhicule par son ID
    @GetMapping("/{id}")
    public Vehicule getVehiculeById(@PathVariable Long id) {
        return vehiculeService.getVehiculeById(id);
    }

    // Supprimer un véhicule
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicule(@PathVariable Long id) {
        System.out.println("Suppression demandée pour ID: " + id);
        vehiculeService.deleteVehicule(id); // Vérifiez cette méthode
        return ResponseEntity.noContent().build();
    }
}