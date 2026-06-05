package com.taxibrousse.gestionvoyage.service;

import com.taxibrousse.gestionvoyage.model.Maintenance;
import com.taxibrousse.gestionvoyage.repository.MaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    public List<Maintenance> getAllMaintenances() {
        return maintenanceRepository.findAll();
    }

    public Maintenance createMaintenance(Maintenance maintenance) {
        return maintenanceRepository.save(maintenance);
    }

    public Maintenance getMaintenanceById(Long id) {
        return maintenanceRepository.findById(id).orElse(null);
    }

    public void deleteMaintenance(Long id) {
        maintenanceRepository.deleteById(id);
    }
}