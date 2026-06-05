package com.taxibrousse.gestionvoyage.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service // Indique que c'est un service Spring
public class PaymentService {

    @Value("${stripe.secret.key}") // Récupère la clé secrète Stripe depuis application.properties
    private String stripeSecretKey;

    public String createStripeCustomer(String email, String name) {
        try {
            // Paramètres pour créer un Customer dans Stripe
            CustomerCreateParams params = CustomerCreateParams.builder()
                    .setName(name)
                    .setEmail(email)
                    .build();

            // Crée un Customer dans Stripe
            Customer customer = Customer.create(params);

            // Retourne l'ID du Customer créé
            return customer.getId();
        } catch (StripeException e) {
            throw new RuntimeException("Erreur lors de la création du compte Stripe : " + e.getMessage());
        }
    }
}