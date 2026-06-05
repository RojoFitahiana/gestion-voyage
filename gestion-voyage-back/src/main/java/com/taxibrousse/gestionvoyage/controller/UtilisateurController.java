package com.taxibrousse.gestionvoyage.controller;

import com.taxibrousse.gestionvoyage.model.Utilisateur;
import com.taxibrousse.gestionvoyage.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    // Récupérer tous les utilisateurs
    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        return ResponseEntity.ok(utilisateurs);
    }

    // Récupérer tous les chauffeurs
    @GetMapping("/chauffeurs")
    public ResponseEntity<List<Utilisateur>> getChauffeurs() {
        List<Utilisateur> chauffeurs = utilisateurService.getChauffeurs();
        return ResponseEntity.ok(chauffeurs);
    }

    // Récupérer les chauffeurs disponibles
    @GetMapping("/chauffeurs/disponibles")
    public ResponseEntity<List<Utilisateur>> getAvailableChauffeurs() {
        List<Utilisateur> chauffeurs = utilisateurService.getAvailableChauffeurs();
        return ResponseEntity.ok(chauffeurs);
    }

    // Récupérer tous les clients
    @GetMapping("/clients")  // Ajout de cet endpoint
    public ResponseEntity<List<Utilisateur>> getClients() {
        List<Utilisateur> clients = utilisateurService.getClients();  // Nouvelle méthode à implémenter
        return ResponseEntity.ok(clients);
    }

    // Créer un nouvel utilisateur
    @PostMapping
    public ResponseEntity<Utilisateur> createUtilisateur(@RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur createdUtilisateur = utilisateurService.createUtilisateur(utilisateur);
            return ResponseEntity.status(201).body(createdUtilisateur);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Mettre à jour un utilisateur
    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> updateUtilisateur(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur updatedUtilisateur = utilisateurService.updateUtilisateur(id, utilisateur);
            return ResponseEntity.ok(updatedUtilisateur);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Récupérer un utilisateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Long id) {
        try {
            Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);
            return ResponseEntity.ok(utilisateur);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        try {
            utilisateurService.deleteUtilisateur(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}