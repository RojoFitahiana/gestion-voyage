package com.taxibrousse.gestionvoyage.service;

import com.taxibrousse.gestionvoyage.model.Notification;
import com.taxibrousse.gestionvoyage.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}