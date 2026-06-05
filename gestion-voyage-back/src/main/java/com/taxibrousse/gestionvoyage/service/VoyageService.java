package com.taxibrousse.gestionvoyage.service;

import com.taxibrousse.gestionvoyage.model.Voyage;
import com.taxibrousse.gestionvoyage.repository.VoyageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

@Service
public class VoyageService {

    @Autowired
    private VoyageRepository voyageRepository;

    // Récupérer tous les voyages (actifs et inactifs)
    public List<Voyage> getAllVoyages() {
        return voyageRepository.findAll();
    }

    public List<Voyage> getVoyagesByStatut(String statut) {
        return voyageRepository.findByStatut(statut);
    }

    // Récupérer uniquement les voyages actifs
    public List<Voyage> getAllActiveVoyages() {
        return voyageRepository.findAll().stream()
                .filter(v -> "actif".equals(v.getStatut()) || "planifié".equals(v.getStatut()) || "en cours".equals(v.getStatut()))
                .collect(Collectors.toList());
    }

    // Créer un nouveau voyage
    public Voyage createVoyage(Voyage voyage) {
        // Vérifier si le voyage est planifié
        if ("planifié".equals(voyage.getStatut())) {
            // Permettre l’ajout même si le chauffeur ou le véhicule est occupé
            Voyage savedVoyage = voyageRepository.save(voyage);
            savedVoyage.setDateArrivee(null); // Assurer que dateArrivee est null pour un voyage planifié
            return voyageRepository.save(savedVoyage);
        } else {
            // Pour les autres statuts, vérifier la disponibilité du chauffeur et du véhicule
            List<Voyage> chauffeurConflicts = voyageRepository.findAll().stream()
                    .filter(v -> (v.getStatut().equals("en cours") || v.getStatut().equals("planifié")) &&
                            v.getChauffeur().getId().equals(voyage.getChauffeur().getId()))
                    .collect(Collectors.toList());
            if (!chauffeurConflicts.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le chauffeur est déjà assigné à un voyage en cours ou planifié.");
            }

            List<Voyage> vehiculeConflicts = voyageRepository.findAll().stream()
                    .filter(v -> (v.getStatut().equals("en cours") || v.getStatut().equals("planifié")) &&
                            v.getVehicule().getId().equals(voyage.getVehicule().getId()))
                    .collect(Collectors.toList());
            if (!vehiculeConflicts.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le véhicule est déjà assigné à un voyage en cours ou planifié.");
            }

            voyage.setDateArrivee(null); // Assurer que dateArrivee est null pour les autres statuts si non définie
            return voyageRepository.save(voyage);
        }
    }

    public Voyage updateVoyage(Long id, Voyage voyage) {
        Optional<Voyage> existingVoyageOpt = voyageRepository.findById(id);
        if (existingVoyageOpt.isPresent()) {
            Voyage existingVoyage = existingVoyageOpt.get();
            // Vérifier si le chauffeur est déjà dans un autre voyage en cours ou planifié
            List<Voyage> chauffeurConflicts = voyageRepository.findAll().stream()
                    .filter(v -> (v.getStatut().equals("en cours") || v.getStatut().equals("planifié")) &&
                            v.getChauffeur().getId().equals(voyage.getChauffeur().getId()) &&
                            !v.getId().equals(id))
                    .collect(Collectors.toList());
            if (!chauffeurConflicts.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le chauffeur est déjà assigné à un autre voyage en cours ou planifié.");
            }

            // Vérifier si le véhicule est déjà dans un autre voyage en cours ou planifié
            List<Voyage> vehiculeConflicts = voyageRepository.findAll().stream()
                    .filter(v -> (v.getStatut().equals("en cours") || v.getStatut().equals("planifié")) &&
                            v.getVehicule().getId().equals(voyage.getVehicule().getId()) &&
                            !v.getId().equals(id))
                    .collect(Collectors.toList());
            if (!vehiculeConflicts.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le véhicule est déjà assigné à un autre voyage en cours ou planifié.");
            }

            existingVoyage.setTrajet(voyage.getTrajet());
            existingVoyage.setVehicule(voyage.getVehicule());
            existingVoyage.setChauffeur(voyage.getChauffeur());
            existingVoyage.setDateDepart(voyage.getDateDepart());
            existingVoyage.setDateArrivee(voyage.getDateArrivee());
            existingVoyage.setStatut(voyage.getStatut());
            return voyageRepository.save(existingVoyage);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Voyage avec l'ID " + id + " non trouvé");
    }

    // Supprimer un voyage
    public void deleteVoyage(Long id) {
        if (voyageRepository.existsById(id)) {
            voyageRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Voyage avec l'ID " + id + " non trouvé");
        }
    }

    // Désactiver un voyage
    public Voyage desactiverVoyage(Long id) {
        Voyage voyage = voyageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voyage avec l'ID " + id + " non trouvé"));
        voyage.setStatut("inactif");
        return voyageRepository.save(voyage);
    }

    // Réactiver un voyage
    public Voyage activerVoyage(Long id) {
        Voyage voyage = voyageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voyage avec l'ID " + id + " non trouvé"));
        voyage.setStatut("actif");
        return voyageRepository.save(voyage);
    }

    // Démarrer un voyage
    public Voyage demarrerVoyage(Long id) {
        Voyage voyage = voyageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voyage avec l'ID " + id + " non trouvé"));
        if (!"planifié".equals(voyage.getStatut()) && !"actif".equals(voyage.getStatut())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le voyage ne peut être démarré que s'il est planifié ou actif");
        }
        voyage.setStatut("en cours");
        return voyageRepository.save(voyage);
    }

    // Terminer un voyage
    public Voyage terminerVoyage(Long id) {
        Voyage voyage = voyageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voyage avec l'ID " + id + " non trouvé"));
        if (!"en cours".equals(voyage.getStatut())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le voyage ne peut être terminé que s'il est en cours");
        }
        voyage.setStatut("terminé");
        voyage.setDateArrivee(LocalDateTime.now()); // Ajoute automatiquement la date/heure actuelle
        return voyageRepository.save(voyage);
    }

    // Récupérer un voyage par ID avec Optional
    public Optional<Voyage> getVoyageById(Long id) {
        return voyageRepository.findById(id);
    }
}