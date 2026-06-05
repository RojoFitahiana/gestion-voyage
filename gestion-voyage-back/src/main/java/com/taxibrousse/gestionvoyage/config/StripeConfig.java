package com.taxibrousse.gestionvoyage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import com.stripe.Stripe;

@Configuration // Indique que c'est une classe de configuration
public class StripeConfig {

    @Value("${stripe.secret.key}") // Récupère la clé secrète depuis application.properties
    private String stripeSecretKey;

    @PostConstruct // Cette méthode sera exécutée après l'initialisation du bean
    public void init() {
        Stripe.apiKey = stripeSecretKey; // Configure la clé API Stripe
    }
}