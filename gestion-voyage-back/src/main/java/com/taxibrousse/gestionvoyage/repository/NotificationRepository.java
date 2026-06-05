package com.taxibrousse.gestionvoyage.repository;

import com.taxibrousse.gestionvoyage.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}