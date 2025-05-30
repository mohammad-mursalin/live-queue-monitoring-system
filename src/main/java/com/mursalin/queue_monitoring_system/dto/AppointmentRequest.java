package com.mursalin.queue_monitoring_system.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    private String userId;
    private String doctorId;
    private LocalDate appointmentDate;
}
