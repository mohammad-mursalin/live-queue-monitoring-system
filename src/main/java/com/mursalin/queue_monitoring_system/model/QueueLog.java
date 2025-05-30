package com.mursalin.queue_monitoring_system.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "queue_logs")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class QueueLog {
    @Id
    private String id;
    private String doctorId;
    private long currentSerial;
    private boolean isDelayed;
    private LocalDateTime lastUpdated;
}
