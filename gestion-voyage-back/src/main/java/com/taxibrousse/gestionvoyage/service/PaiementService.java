package com.taxibrousse.gestionvoyage.service;

import com.taxibrousse.gestionvoyage.model.Paiement;
import com.taxibrousse.gestionvoyage.repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaiementService {

    @Autowired
    private PaiementRepository paiementRepository;

    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    public Paiement createPaiement(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    public Paiement getPaiementById(Long id) {
        return paiementRepository.findById(id).orElse(null);
    }

    public void deletePaiement(Long id) {
        paiementRepository.deleteById(id);
    }
}