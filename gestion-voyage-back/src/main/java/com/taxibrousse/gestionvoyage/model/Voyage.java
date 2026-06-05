package com.taxibrousse.gestionvoyage.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Voyage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chauffeur_id", nullable = false)
    private Utilisateur chauffeur;

    private LocalDateTime dateDepart;
    private LocalDateTime dateArrivee;
    private String statut; // planifié, en cours, terminé, annulé

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "voyage_occupied_seats", joinColumns = @JoinColumn(name = "voyage_id"))
    private Set<Integer> occupiedSeats = new HashSet<>(); // Liste des places occupées

    // Méthode pour ajouter une place occupée avec validation
    public void addOccupiedSeat(int seatNumber) {
        if (vehicule == null) {
            throw new IllegalStateException("Le véhicule n'est pas défini pour ce voyage.");
        }
        int totalSeats = vehicule.getNombrePlaces();
        if (seatNumber < 1 || seatNumber > totalSeats) {
            throw new IllegalArgumentException("Numéro de siège " + seatNumber + " invalide. Doit être entre 1 et " + totalSeats + ".");
        }
        if (occupiedSeats.contains(seatNumber)) {
            throw new IllegalArgumentException("Le siège " + seatNumber + " est déjà occupé.");
        }
        if (getAvailableSeats() <= 0) {
            throw new IllegalStateException("Aucune place disponible dans ce voyage.");
        }
        this.occupiedSeats.add(seatNumber);
    }

    // Calcul des sièges disponibles
    public int getAvailableSeats() {
        if (vehicule == null) {
            return 0; // Ou une autre logique par défaut
        }
        return this.vehicule.getNombrePlaces() - this.occupiedSeats.size();
    }
}