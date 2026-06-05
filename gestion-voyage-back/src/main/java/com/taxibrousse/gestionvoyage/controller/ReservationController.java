package com.taxibrousse.gestionvoyage.controller;

import com.taxibrousse.gestionvoyage.model.Reservation;
import com.taxibrousse.gestionvoyage.model.Utilisateur;
import com.taxibrousse.gestionvoyage.repository.UtilisateurRepository;
import com.taxibrousse.gestionvoyage.repository.VoyageRepository;
import com.taxibrousse.gestionvoyage.service.ReservationService;
import com.taxibrousse.gestionvoyage.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private PaymentService paymentService;

     @Autowired
    private VoyageRepository voyageRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        logger.info("Récupération de toutes les réservations");
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

   @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        try {
            logger.info("Création d'une nouvelle réservation: {}", reservation);
            Reservation createdReservation = reservationService.createReservation(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
        } catch (IllegalArgumentException e) {
            logger.warn("Erreur de validation lors de la création: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la création: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody Map<String, Object> paymentRequest) {
        try {
            // Récupérez le montant depuis la requête
            Long amount = ((Number) paymentRequest.get("amount")).longValue(); // Montant en cents

            // Créez un Payment Intent avec Stripe
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount)
                    .setCurrency("eur") // Devise (par exemple, EUR)
                    .addPaymentMethodType("card") // Type de méthode de paiement
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Renvoyez le clientSecret au frontend
            return ResponseEntity.ok(Map.of("clientSecret", paymentIntent.getClientSecret()));
        } catch (StripeException e) {
            logger.error("Erreur Stripe lors de la création du Payment Intent: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur Stripe : " + e.getMessage());
        }
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<?> confirmReservation(@PathVariable Long id) {
        logger.info("Tentative de confirmation de la réservation ID: {}", id);
        try {
            Reservation confirmedReservation = reservationService.confirmReservation(id);
            logger.info("Réservation confirmée avec succès: ID={}", id);
            return ResponseEntity.ok(confirmedReservation);
        } catch (IllegalArgumentException e) {
            logger.warn("Réservation non trouvée ou invalide: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            logger.warn("État de la réservation incompatible: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la confirmation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur : " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        logger.info("Suppression de la réservation ID: {}", id);
        try {
            reservationService.deleteReservation(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.warn("Réservation non trouvée pour suppression: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}