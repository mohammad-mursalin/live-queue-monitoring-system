package com.mursalin.queue_monitoring_system.controller;

import com.mursalin.queue_monitoring_system.model.QueueLog;
import com.mursalin.queue_monitoring_system.service.QueueService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/queue")
public class QueueController {

    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    // Get current queue status
    @GetMapping("/{doctorId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<QueueLog> getQueueStatus(@PathVariable String doctorId) {
        return ResponseEntity.ok(queueService.getCurrentQueueStatus(doctorId));
    }

    // Admin updates current serial (broadcasts to users)
    @PatchMapping("/{doctorId}/serial")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QueueLog> updateSerial(
            @PathVariable String doctorId,
            @RequestParam @Min(1) int newSerial) {
        return ResponseEntity.ok(queueService.updateCurrentSerial(doctorId, newSerial));
    }


    @GetMapping("/{doctorId}/wait-time")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Integer> getWaitTime(
            @PathVariable String doctorId,
            @RequestParam long serialNumber) {
        return ResponseEntity.ok(queueService.calculateWaitTime(doctorId, serialNumber));
    }
}
