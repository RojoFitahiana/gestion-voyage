package com.taxibrousse.gestionvoyage.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String description; // Raison de la maintenance
    private double cout; // Coût de la maintenance
}