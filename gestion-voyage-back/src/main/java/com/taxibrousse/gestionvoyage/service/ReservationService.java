package com.taxibrousse.gestionvoyage.service;

import com.taxibrousse.gestionvoyage.model.Reservation;
import com.taxibrousse.gestionvoyage.model.Utilisateur;
import com.taxibrousse.gestionvoyage.model.Voyage;
import com.taxibrousse.gestionvoyage.repository.ReservationRepository;
import com.taxibrousse.gestionvoyage.repository.UtilisateurRepository;
import com.taxibrousse.gestionvoyage.repository.VoyageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private VoyageRepository voyageRepository;

    @Autowired
    private JavaMailSender mailSender; // Injection de JavaMailSender

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation createReservation(Reservation reservation) {
        if (reservation.getReservedSeats() == null) {
            reservation.setReservedSeats(new HashSet<>());
        }
        if (reservation.getOccupiedSeats() == null) {
            reservation.setOccupiedSeats(new HashSet<>());
        }

        Utilisateur client = utilisateurRepository.findById(reservation.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable"));

        Voyage voyage = voyageRepository.findById(reservation.getVoyage().getId())
                .orElseThrow(() -> new IllegalArgumentException("Voyage introuvable"));

        if (voyage.getAvailableSeats() < reservation.getNombrePlacesReservees()) {
            throw new IllegalArgumentException("Pas assez de places disponibles.");
        }

        List<Reservation> existingReservations = reservationRepository.findByVoyageId(voyage.getId());
        Set<Integer> allOccupiedSeats = new HashSet<>(voyage.getOccupiedSeats());
        for (Reservation existing : existingReservations) {
            allOccupiedSeats.addAll(existing.getOccupiedSeats());
        }

        for (Integer seat : reservation.getReservedSeats()) {
            if (allOccupiedSeats.contains(seat)) {
                throw new IllegalArgumentException("Place " + seat + " déjà occupée.");
            }
        }

        reservation.setClient(client);
        reservation.setVoyage(voyage);
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setPlacesOccupees(0);

        String requestedStatut = reservation.getStatut() != null ? reservation.getStatut() : "en attente";
        reservation.setStatut("en attente");

        reservation = reservationRepository.save(reservation);

        if ("confirmé".equals(requestedStatut)) {
            reservation = confirmReservation(reservation.getId());
        }

        return reservation;
    }

    public Reservation confirmReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable"));

        Voyage voyage = reservation.getVoyage();
        List<Reservation> existingReservations = reservationRepository.findByVoyageId(voyage.getId());
        Set<Integer> allReservedSeats = new HashSet<>(voyage.getOccupiedSeats());
        for (Reservation existing : existingReservations) {
            if (!existing.getId().equals(reservation.getId())) {
                allReservedSeats.addAll(existing.getReservedSeats());
                allReservedSeats.addAll(existing.getOccupiedSeats());
            }
        }

        for (Integer seat : reservation.getReservedSeats()) {
            if (allReservedSeats.contains(seat)) {
                throw new IllegalArgumentException("Place " + seat + " déjà réservée ou occupée par une autre réservation.");
            }
        }

        reservation.confirmReservation();
        voyageRepository.save(voyage);
        reservation = reservationRepository.save(reservation);

        // Envoi de l'email de confirmation
        sendConfirmationEmail(reservation);

        return reservation;
    }

    // Méthode pour envoyer l'email de confirmation
    private void sendConfirmationEmail(Reservation reservation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(reservation.getClient().getEmail());
        message.setSubject("Confirmation de votre réservation");
        message.setText(
                "Bonjour " + reservation.getClient().getPrenom() + " " + reservation.getClient().getNom() + ",\n\n" +
                        "Votre réservation pour le voyage suivant a été confirmée avec succès :\n" +
                        "Voyage ID : " + reservation.getVoyage().getId() + "\n" +
                        "Départ : " + reservation.getVoyage().getDateDepart() + "\n" +
                        "Arrivée : " + reservation.getVoyage().getDateArrivee() + "\n" +
                        "Nombre de places réservées : " + reservation.getPlacesOccupees() + "\n" +
                        "Sièges occupés : " + reservation.getOccupiedSeats() + "\n\n" +
                        "Merci de voyager avec nous !\n" +
                        "L'équipe TransMada"
        );
        message.setFrom("njahrojo292@gmail.com"); // Doit correspondre à spring.mail.username
        mailSender.send(message);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable"));

        Voyage voyage = reservation.getVoyage();
        if ("confirmé".equals(reservation.getStatut())) {
            voyage.getOccupiedSeats().removeAll(reservation.getOccupiedSeats());
            voyageRepository.save(voyage);
        }
        reservationRepository.delete(reservation);
    }

    public List<Reservation> findByVoyageId(Long voyageId) {
        return reservationRepository.findByVoyageId(voyageId);
    }
}