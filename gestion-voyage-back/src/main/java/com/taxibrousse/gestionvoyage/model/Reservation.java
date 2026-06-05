package com.taxibrousse.gestionvoyage.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Utilisateur client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voyage_id", nullable = false)
    private Voyage voyage;

    private int nombrePlacesReservees;
    private LocalDateTime dateReservation;
    private String statut; // "confirmé", "annulé", "en attente"

    @Column(name = "places_occupees", nullable = false)
    private int placesOccupees; // Champ requis pour une colonne NOT NULL

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "reservation_reserved_seats", joinColumns = @JoinColumn(name = "reservation_id"))
    private Set<Integer> reservedSeats = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "reservation_occupied_seats", joinColumns = @JoinColumn(name = "reservation_id"))
    private Set<Integer> occupiedSeats = new HashSet<>();

    public Reservation() {
        this.reservedSeats = new HashSet<>();
        this.occupiedSeats = new HashSet<>();
        this.placesOccupees = 0; // Valeur par défaut pour NOT NULL
        this.statut = "en attente"; // Statut par défaut
    }

    public void addReservedSeat(int seatNumber) {
        if (this.voyage == null) {
            throw new IllegalStateException("Aucun voyage associé à cette réservation.");
        }
        if (this.voyage.getAvailableSeats() <= 0) {
            throw new IllegalArgumentException("Aucune place disponible dans le voyage.");
        }
        if (this.voyage.getOccupiedSeats().contains(seatNumber)) {
            throw new IllegalArgumentException("Place " + seatNumber + " déjà occupée.");
        }
        if (seatNumber < 1 || seatNumber > this.voyage.getVehicule().getNombrePlaces()) {
            throw new IllegalArgumentException("Numéro de siège " + seatNumber + " invalide.");
        }
        this.reservedSeats.add(seatNumber);
        this.nombrePlacesReservees = this.reservedSeats.size();
    }

    public void confirmReservation() {
        if (!"en attente".equals(this.statut)) {
            throw new IllegalStateException("La réservation doit être en attente pour être confirmée.");
        }
        if (this.voyage == null) {
            throw new IllegalStateException("Aucun voyage associé à cette réservation.");
        }

        // Vérifier que les sièges réservés sont toujours disponibles
        Set<Integer> voyageOccupiedSeats = this.voyage.getOccupiedSeats();
        for (Integer seat : this.reservedSeats) {
            if (voyageOccupiedSeats.contains(seat)) {
                throw new IllegalStateException("Le siège " + seat + " est déjà occupé dans le voyage.");
            }
        }

        // Confirmer la réservation
        this.statut = "confirmé";
        for (Integer seat : this.reservedSeats) {
            this.voyage.addOccupiedSeat(seat); // Ajoute les sièges au voyage
        }
        this.occupiedSeats.addAll(this.reservedSeats);
        this.placesOccupees = this.occupiedSeats.size();
        this.reservedSeats.clear(); // Les sièges réservés sont maintenant occupés
    }

    // Méthode pour annuler une réservation (optionnel, selon votre besoin)
    public void cancelReservation() {
        if ("confirmé".equals(this.statut)) {
            throw new IllegalStateException("Une réservation confirmée ne peut pas être annulée directement. Supprimez-la pour libérer les sièges.");
        }
        this.statut = "annulé";
        this.reservedSeats.clear(); // Libère les sièges réservés non confirmés
        this.nombrePlacesReservees = 0;
    }
}