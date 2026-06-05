package com.taxibrousse.gestionvoyage.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trajet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String villeDepart;
    private String villeArrivee;
    private double distance; // en km
    private double dureeEstimee; // en heures
    private String statut;
    private double prix;

    @OneToMany(mappedBy = "trajet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("trajet") // Éviter la boucle infinie lors de la sérialisation
    private List<Voyage> voyages = new ArrayList<>();
}