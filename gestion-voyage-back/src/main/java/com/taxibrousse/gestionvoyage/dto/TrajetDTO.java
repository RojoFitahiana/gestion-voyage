package com.taxibrousse.gestionvoyage.dto;

import java.util.List;

public class TrajetDTO {
    private Long id;
    private String villeDepart;
    private String villeArrivee;
    private double distance;
    private String dureeEstimee;
    private String statut;
    private double prix;
    private List<VoyageDTO> voyages;

    public TrajetDTO(Long id, String villeDepart, String villeArrivee, double distance, String dureeEstimee, String statut, double prix, List<VoyageDTO> voyages) {
        this.id = id;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.distance = distance;
        this.dureeEstimee = dureeEstimee;
        this.statut = statut;
        this.prix = prix;
        this.voyages = voyages;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getVilleDepart() { return villeDepart; }
    public void setVilleDepart(String villeDepart) { this.villeDepart = villeDepart; }
    public String getVilleArrivee() { return villeArrivee; }
    public void setVilleArrivee(String villeArrivee) { this.villeArrivee = villeArrivee; }
    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }
    public String getDureeEstimee() { return dureeEstimee; }
    public void setDureeEstimee(String dureeEstimee) { this.dureeEstimee = dureeEstimee; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    public List<VoyageDTO> getVoyages() { return voyages; }
    public void setVoyages(List<VoyageDTO> voyages) { this.voyages = voyages; }
}