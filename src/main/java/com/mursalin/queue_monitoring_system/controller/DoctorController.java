package com.mursalin.queue_monitoring_system.controller;

import com.mursalin.queue_monitoring_system.model.Doctor;
import com.mursalin.queue_monitoring_system.service.DoctorService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/add-doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    public ResponseEntity<?> addDoctor(@RequestBody @NotNull Doctor doctor) {
        return doctorService.addDoctor(doctor);
    }
}
