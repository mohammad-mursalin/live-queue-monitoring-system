package com.mursalin.queue_monitoring_system.service;

import com.mursalin.queue_monitoring_system.model.Doctor;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

public interface DoctorService {
    ResponseEntity<?> getAllDoctors();

    ResponseEntity<?> addDoctor(@NotNull Doctor doctor);
}
