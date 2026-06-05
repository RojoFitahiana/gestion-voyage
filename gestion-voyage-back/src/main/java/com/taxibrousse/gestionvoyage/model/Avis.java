package com.taxibrousse.gestionvoyage.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Avis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Utilisateur client;

    @ManyToOne
    @JoinColumn(name = "voyage_id", nullable = false)
    private Voyage voyage;

    private int note; // par exemple, de 1 à 5
    private String commentaire;
    private LocalDateTime dateAvis;

}