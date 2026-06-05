package com.taxibrousse.gestionvoyage.repository;

import com.taxibrousse.gestionvoyage.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByVoyageId(Long voyageId);
}