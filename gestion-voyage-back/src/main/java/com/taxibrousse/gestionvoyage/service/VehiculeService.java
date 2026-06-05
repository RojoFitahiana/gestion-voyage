package com.taxibrousse.gestionvoyage.service;

import com.taxibrousse.gestionvoyage.model.Vehicule;
import com.taxibrousse.gestionvoyage.repository.VehiculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculeService {

    @Autowired
    private VehiculeRepository vehiculeRepository;

    public List<Vehicule> getAllVehicules() {
        return vehiculeRepository.findAll();
    }

    public Vehicule createVehicule(Vehicule vehicule) {
        return vehiculeRepository.save(vehicule);
    }

    public Vehicule getVehiculeById(Long id) {
        return vehiculeRepository.findById(id).orElse(null);
    }

    public void deleteVehicule(Long id) {
        vehiculeRepository.deleteById(id);
    }

    public Vehicule updateVehicule(Long id, Vehicule vehicule) {
        Optional<Vehicule> existingVehiculeOpt = vehiculeRepository.findById(id);
        if (existingVehiculeOpt.isPresent()) {
            Vehicule existingVehicule = existingVehiculeOpt.get();
            existingVehicule.setImmatriculation(vehicule.getImmatriculation());
            existingVehicule.setMarque(vehicule.getMarque());
            existingVehicule.setModele(vehicule.getModele());
            existingVehicule.setNombrePlaces(vehicule.getNombrePlaces());
            existingVehicule.setStatut(vehicule.getStatut());
            return vehiculeRepository.save(existingVehicule);
        }
        return null; // Ou tu peux lever une exception si le véhicule n'existe pas
    }
}