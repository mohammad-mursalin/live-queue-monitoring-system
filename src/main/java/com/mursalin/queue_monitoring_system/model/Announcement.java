package com.mursalin.queue_monitoring_system.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "announcements")
@Data
public class Announcement {
    @Id
    private String id;
    private String message;
    private LocalDateTime createdAt;
}
