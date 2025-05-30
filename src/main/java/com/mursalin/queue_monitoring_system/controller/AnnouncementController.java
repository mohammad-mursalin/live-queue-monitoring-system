package com.mursalin.queue_monitoring_system.controller;

import com.mursalin.queue_monitoring_system.model.Announcement;
import com.mursalin.queue_monitoring_system.service.AnnouncementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    // Create announcement (Admin only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Announcement> createAnnouncement(
            @Valid @RequestBody String message) {
        return ResponseEntity.ok(announcementService.createAnnouncement(message));
    }

    // Get recent announcements
    @GetMapping
    public ResponseEntity<List<Announcement>> getRecentAnnouncements(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(announcementService.getRecentAnnouncements(limit));
    }
}
