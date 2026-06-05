package com.taxibrousse.gestionvoyage.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String immatriculation;
    private String marque;
    private String modele;
    private int nombrePlaces;
    private String statut; // disponible, en maintenance, etc.

    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Maintenance> maintenances;
}