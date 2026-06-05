package com.taxibrousse.gestionvoyage.service;

import com.taxibrousse.gestionvoyage.dto.AvisCreationDTO;
import com.taxibrousse.gestionvoyage.model.Avis;
import com.taxibrousse.gestionvoyage.model.Utilisateur;
import com.taxibrousse.gestionvoyage.model.Voyage;
import com.taxibrousse.gestionvoyage.repository.AvisRepository;
import com.taxibrousse.gestionvoyage.repository.UtilisateurRepository;
import com.taxibrousse.gestionvoyage.repository.VoyageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AvisService {

    @Autowired
    private AvisRepository avisRepository;
    @Autowired
    private VoyageRepository voyageRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Avis> getAllAvis() {
        return avisRepository.findAll();
    }

    public Avis createAvis(AvisCreationDTO dto) {
        Utilisateur client = utilisateurRepository.findById(dto.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé"));

        Voyage voyage = voyageRepository.findById(dto.getVoyageId())
                .orElseThrow(() -> new IllegalArgumentException("Voyage non trouvé"));

        Avis avis = new Avis();
        avis.setClient(client);
        avis.setVoyage(voyage);
        avis.setNote(dto.getNote());
        avis.setCommentaire(dto.getCommentaire());
        avis.setDateAvis(dto.getDateAvis());

        return avisRepository.save(avis);
    }

    public Avis getAvisById(Long id) {
        return avisRepository.findById(id).orElse(null);
    }

    public void deleteAvis(Long id) {
        avisRepository.deleteById(id);
    }

    public Avis updateAvis(Long id, Avis avis) {
        Optional<Avis> existingAvisOpt = avisRepository.findById(id);
        if (existingAvisOpt.isPresent()) {
            Avis existingAvis = existingAvisOpt.get();
            existingAvis.setClient(avis.getClient());
            existingAvis.setVoyage(avis.getVoyage());
            existingAvis.setNote(avis.getNote());
            existingAvis.setCommentaire(avis.getCommentaire());
            existingAvis.setDateAvis(avis.getDateAvis());
            return avisRepository.save(existingAvis);
        }
        return null; // Ou lever une exception
    }
}