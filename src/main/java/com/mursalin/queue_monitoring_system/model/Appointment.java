package com.mursalin.queue_monitoring_system.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    private String id;

    // Reference to User document (could be embedded or reference)
    private String userId;

    // Reference to Doctor document
    private String doctorId;

    private LocalDate appointmentDate;
    private LocalDateTime checkInTime;       // When user arrives at hospital
    private LocalDateTime sessionStartTime;  // When doctor starts seeing user
    private LocalDateTime completionTime;    // When consultation ends

    private long serialNumber;               // Queue position (1, 2, 3...)

    private AppointmentStatus status;                  // "SCHEDULED", "WAITING", "IN_SESSION", "COMPLETED", "CANCELLED"

    // Estimated fields (can be calculated on demand)
    private Integer estimatedWaitMinutes;    // Populated when status=WAITING

    // Calculate duration of consultation
    public Long getConsultationDurationMinutes() {
        if (sessionStartTime == null || completionTime == null) return null;
        return java.time.Duration.between(sessionStartTime, completionTime).toMinutes();
    }
}
