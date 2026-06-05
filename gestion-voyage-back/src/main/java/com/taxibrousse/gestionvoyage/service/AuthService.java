package com.taxibrousse.gestionvoyage.service;

import com.taxibrousse.gestionvoyage.dto.LoginRequest;
import com.taxibrousse.gestionvoyage.model.Utilisateur;
import com.taxibrousse.gestionvoyage.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

@Slf4j
@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;

    public AuthService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public Utilisateur login(LoginRequest request) {
        return utilisateurRepository.findByEmail(request.getEmail())
                .filter(user -> user.getMotDePasse().equals(request.getPassword())) // Comparaison simple
                .orElseThrow(() -> new RuntimeException("Identifiants incorrects"));
    }

    // Dans AuthService.java
    public Utilisateur adminLogin(LoginRequest request) {
        return utilisateurRepository.findByEmail(request.getEmail())
                .filter(user -> user.getMotDePasse().equals(request.getPassword()))
                .filter(user -> "admin".equalsIgnoreCase(user.getRole().trim()))
                .orElseThrow(() -> new RuntimeException("Accès admin refusé"));
    }

    private boolean validatePassword(Utilisateur user, String rawPassword) {
        // Implémentez la logique de validation de mot de passe
        // Exemple basique (à remplacer par du hash en production)
        return user.getMotDePasse().equals(rawPassword);
    }

}
