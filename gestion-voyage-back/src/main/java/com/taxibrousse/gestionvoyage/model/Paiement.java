package com.taxibrousse.gestionvoyage.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    private double montant;
    private String methodePaiement; // espèces, mobile money, carte bancaire, etc.
    private LocalDateTime datePaiement;
    private String statut; // payé, en attente, échoué
}