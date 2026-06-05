package com.taxibrousse.gestionvoyage.controller;

import com.taxibrousse.gestionvoyage.dto.LoginRequest;
import com.taxibrousse.gestionvoyage.model.Utilisateur;
import com.taxibrousse.gestionvoyage.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Utilisateur> login(@RequestBody LoginRequest request) {
        System.out.println("Login attempt for email: " + request.getEmail());
        try {
            Utilisateur user = authService.login(request);
            if (user != null) {
                System.out.println("Login successful for user: " + user.getNom());
                return ResponseEntity.ok(user);
            } else {
                System.err.println("User not found or password mismatch");
                throw new RuntimeException("Identifiants incorrects");
            }
        } catch (RuntimeException e) {
            System.err.println("Login failed: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }



    @PostMapping("/admin/login")
    public ResponseEntity<Utilisateur> adminLogin(@RequestBody LoginRequest request) {
        System.out.println("Tentative connexion admin pour email : " + request.getEmail());

        try {
            Utilisateur admin = authService.adminLogin(request);

            if (admin.getRole().equals("admin")) {
                System.out.println("Connexion admin réussie pour : " + admin.getNom());
                return ResponseEntity.ok(admin);
            }

            System.err.println("Accès refusé - Rôle : " + admin.getRole());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès non autorisé");

        } catch (RuntimeException e) {
            System.err.println("Erreur connexion admin : " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }
}

