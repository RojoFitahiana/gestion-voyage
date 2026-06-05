package com.taxibrousse.gestionvoyage.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.taxibrousse.gestionvoyage.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:4200") // Autorise les requêtes depuis Angular
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody Map<String, Object> paymentRequest) {
        try {
            // Récupérez les données nécessaires
            Long amount = ((Number) paymentRequest.get("amount")).longValue(); // Montant en cents
            String email = (String) paymentRequest.get("email"); // E-mail de l'utilisateur
            String name = (String) paymentRequest.get("name"); // Nom complet

            // Créez un Customer Stripe
            String customerId = paymentService.createStripeCustomer(email, name);

            // Créez un Payment Intent avec Stripe
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount)
                    .setCurrency("eur") // Devise (par exemple, EUR)
                    .setCustomer(customerId) // Associez le Customer ID
                    .addPaymentMethodType("card") // Type de méthode de paiement
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Renvoyez le clientSecret au frontend
            return ResponseEntity.ok(Map.of("clientSecret", paymentIntent.getClientSecret()));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur Stripe : " + e.getMessage());
        }
    }
}