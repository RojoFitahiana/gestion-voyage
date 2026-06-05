package com.taxibrousse.gestionvoyage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Applique à toutes les routes
                .allowedOrigins("http://localhost:4200") // Autorise cet origine
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Méthodes autorisées
                .allowedHeaders("*") // Tous les en-têtes
                .allowCredentials(true); // Si tu utilises des cookies/auth
    }
}