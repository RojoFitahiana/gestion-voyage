package com.taxibrousse.gestionvoyage.dto;

public class VoyageDTO {
    private Long id;
    private String dateVoyage; // Exemple

    public VoyageDTO(Long id, String dateVoyage) {
        this.id = id;
        this.dateVoyage = dateVoyage;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDateVoyage() { return dateVoyage; }
    public void setDateVoyage(String dateVoyage) { this.dateVoyage = dateVoyage; }
}