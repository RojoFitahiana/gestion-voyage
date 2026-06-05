package com.taxibrousse.gestionvoyage.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ReservationDTO {
    private Long id;
    private Long clientId; // Juste l’ID au lieu de l’objet complet
    private Long voyageId; // Juste l’ID au lieu de l’objet complet
    private int nombrePlacesReservees;
    private LocalDateTime dateReservation;
    private String statut;
    private Set<Integer> reservedSeats;
}