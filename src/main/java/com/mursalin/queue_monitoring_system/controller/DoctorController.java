package com.mursalin.queue_monitoring_system.controller;

import com.mursalin.queue_monitoring_system.dto.DoctorDto;
import com.mursalin.queue_monitoring_system.model.Doctor;
import com.mursalin.queue_monitoring_system.service.DoctorService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping("/admin/add-doctor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addDoctor(@RequestBody @NotNull Doctor doctor) {
        return doctorService.addDoctor(doctor);
    }

    @GetMapping("/doctor-list")
    public ResponseEntity<List<DoctorDto>> getDoctorList() {
        return doctorService.getAllDoctors();
    }

    @PutMapping("/update-doctor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorDto> updateDoctor(@RequestBody DoctorDto doctorDto) {
        return doctorService.updateDoctor(doctorDto);
    }

    @DeleteMapping("/remove-doctor/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteDoctor(@PathVariable String id) {
        return doctorService.removeDoctor(id);
    }
}
