package com.mursalin.queue_monitoring_system.service;

import com.mursalin.queue_monitoring_system.model.Announcement;
import com.mursalin.queue_monitoring_system.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public Announcement createAnnouncement(String message) {
        Announcement announcement = new Announcement();
        announcement.setMessage(message);
        announcement.setCreatedAt(LocalDateTime.now());

        Announcement saved = announcementRepository.save(announcement);

        // Broadcast to all connected clients
        messagingTemplate.convertAndSend("/topic/announcements", saved);
        return saved;
    }

    public List<Announcement> getRecentAnnouncements(int limit) {
        return announcementRepository.findTopByOrderByCreatedAtDesc(limit);
    }

    public void broadcastEmergencyAlert(String message) {
        Announcement emergency = new Announcement();
        emergency.setMessage("[URGENT] " + message);
        emergency.setCreatedAt(LocalDateTime.now());

        // High-priority broadcast
        messagingTemplate.convertAndSend("/topic/emergency-alerts", emergency);
    }
}
