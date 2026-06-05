package com.taxibrousse.gestionvoyage.service;

import com.taxibrousse.gestionvoyage.model.Trajet;
import com.taxibrousse.gestionvoyage.model.Voyage;
import com.taxibrousse.gestionvoyage.repository.TrajetRepository;
import com.taxibrousse.gestionvoyage.repository.VoyageRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrajetService {

    private static final Logger logger = LoggerFactory.getLogger(TrajetService.class);

    @Autowired
    private TrajetRepository trajetRepository;

    @Autowired
    private VoyageRepository voyageRepository;

    // Récupérer tous les trajets (actifs et inactifs)
    public List<Trajet> getAllTrajets() {
        return trajetRepository.findAll();
    }

    // Créer un nouveau trajet
    public Trajet createTrajet(Trajet trajet) {
        return trajetRepository.save(trajet);
    }

    // Récupérer un trajet par ID
    public Trajet getTrajetById(Long id) {
        return trajetRepository.findById(id).orElse(null);
    }

    // Mettre à jour un trajet
    // Mettre à jour un trajet
    public Trajet updateTrajet(Long id, Trajet trajet) {
        Optional<Trajet> existingTrajetOpt = trajetRepository.findById(id);
        if (existingTrajetOpt.isPresent()) {
            Trajet existingTrajet = existingTrajetOpt.get();
            existingTrajet.setVilleDepart(trajet.getVilleDepart());
            existingTrajet.setVilleArrivee(trajet.getVilleArrivee());
            existingTrajet.setDistance(trajet.getDistance());
            existingTrajet.setDureeEstimee(trajet.getDureeEstimee());
            existingTrajet.setPrix(trajet.getPrix()); // Ajouté ici !
            existingTrajet.setStatut(trajet.getStatut());
            return trajetRepository.save(existingTrajet);
        }
        return null; // Retourne null si le trajet n'existe pas
    }
    // Supprimer un trajet et ses voyages associés
    public void deleteTrajet(Long id) {
        logger.info("Vérification de l’existence du trajet ID: " + id);
        boolean exists = trajetRepository.existsById(id);
        logger.info("Trajet ID " + id + " existe ? " + exists);
        if (exists) {
            logger.info("Suppression du trajet ID: " + id);
            trajetRepository.deleteById(id);
        } else {
            logger.warn("Trajet avec ID " + id + " non trouvé");
            throw new RuntimeException("Trajet avec l'ID " + id + " non trouvé");
        }
    }

    // Désactiver un trajet
    public Trajet desactiverTrajet(Long id) {
        Trajet trajet = trajetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trajet avec l'ID " + id + " non trouvé"));
        trajet.setStatut("inactif");
        return trajetRepository.save(trajet);
    }

    // Réactiver un trajet
    public Trajet activerTrajet(Long id) {
        Trajet trajet = trajetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trajet avec l'ID " + id + " non trouvé"));
        trajet.setStatut("actif");
        return trajetRepository.save(trajet);
    }
}