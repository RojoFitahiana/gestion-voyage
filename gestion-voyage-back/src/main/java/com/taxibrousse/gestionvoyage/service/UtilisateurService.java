package com.taxibrousse.gestionvoyage.service;

import com.taxibrousse.gestionvoyage.model.Utilisateur;
import com.taxibrousse.gestionvoyage.model.Voyage;
import com.taxibrousse.gestionvoyage.repository.UtilisateurRepository;
import com.taxibrousse.gestionvoyage.repository.VoyageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private VoyageRepository voyageRepository; // Ajout pour vérifier les voyages

    // Récupérer tous les utilisateurs
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    // Créer un nouvel utilisateur
    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        if (utilisateur.getRole() == null || utilisateur.getRole().isEmpty()) {
            throw new IllegalArgumentException("Le rôle de l’utilisateur est requis");
        }
        return utilisateurRepository.save(utilisateur);
    }

    // Récupérer un utilisateur par ID
    public Utilisateur getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur avec l’ID " + id + " non trouvé"));
    }

    // Supprimer un utilisateur
    public void deleteUtilisateur(Long id) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(id);
        if (utilisateurOpt.isPresent()) {
            // Vérifier si le chauffeur est assigné à un voyage en cours ou planifié
            Utilisateur utilisateur = utilisateurOpt.get();
            if ("chauffeur".equals(utilisateur.getRole()) && isChauffeurAssignedToActiveVoyage(id)) {
                throw new RuntimeException("Impossible de supprimer ce chauffeur : il est assigné à un voyage en cours ou planifié");
            }
            utilisateurRepository.deleteById(id);
        } else {
            throw new RuntimeException("Utilisateur avec l’ID " + id + " non trouvé");
        }
    }

    // Mettre à jour un utilisateur
    public Utilisateur updateUtilisateur(Long id, Utilisateur utilisateur) {
        Optional<Utilisateur> existingUtilisateurOpt = utilisateurRepository.findById(id);
        if (existingUtilisateurOpt.isPresent()) {
            Utilisateur existingUtilisateur = existingUtilisateurOpt.get();
            // Vérifier si le rôle change et si le chauffeur est assigné à un voyage actif
            if ("chauffeur".equals(existingUtilisateur.getRole()) &&
                    !utilisateur.getRole().equals("chauffeur") &&
                    isChauffeurAssignedToActiveVoyage(id)) {
                throw new RuntimeException("Impossible de changer le rôle de ce chauffeur : il est assigné à un voyage en cours ou planifié");
            }
            existingUtilisateur.setNom(utilisateur.getNom());
            existingUtilisateur.setPrenom(utilisateur.getPrenom());
            existingUtilisateur.setEmail(utilisateur.getEmail());
            existingUtilisateur.setMotDePasse(utilisateur.getMotDePasse());
            existingUtilisateur.setRole(utilisateur.getRole());
            existingUtilisateur.setTelephone(utilisateur.getTelephone());
            existingUtilisateur.setAdresse(utilisateur.getAdresse());
            return utilisateurRepository.save(existingUtilisateur);
        } else {
            throw new RuntimeException("Utilisateur avec l’ID " + id + " non trouvé");
        }
    }

    // Récupérer tous les chauffeurs
    public List<Utilisateur> getChauffeurs() {
        return utilisateurRepository.findAll().stream()
                .filter(u -> "chauffeur".equals(u.getRole()))
                .collect(Collectors.toList());
    }

    // Récupérer tous les clients
    public List<Utilisateur> getClients() {
        return utilisateurRepository.findAll().stream()
                .filter(u -> "client".equals(u.getRole()))
                .collect(Collectors.toList());
    }

    // Vérifier si un chauffeur est assigné à un voyage en cours ou planifié
    private boolean isChauffeurAssignedToActiveVoyage(Long chauffeurId) {
        return voyageRepository.findAll().stream()
                .anyMatch(v -> (v.getStatut().equals("en cours") || v.getStatut().equals("planifié")) &&
                        v.getChauffeur().getId().equals(chauffeurId));
    }

    // Récupérer les chauffeurs disponibles (non assignés à des voyages en cours ou planifiés)
    public List<Utilisateur> getAvailableChauffeurs() {
        return getChauffeurs().stream()
                .filter(chauffeur -> !isChauffeurAssignedToActiveVoyage(chauffeur.getId()))
                .collect(Collectors.toList());
    }
}