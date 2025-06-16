package com.mursalin.queue_monitoring_system.service;

import com.mursalin.queue_monitoring_system.dto.DoctorDto;
import com.mursalin.queue_monitoring_system.model.Doctor;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DoctorService {
    ResponseEntity<List<DoctorDto>> getAllDoctors();

    ResponseEntity<?> addDoctor(@NotNull Doctor doctor);
}
