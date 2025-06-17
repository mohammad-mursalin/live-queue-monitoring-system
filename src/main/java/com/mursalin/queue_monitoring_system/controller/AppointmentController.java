package com.mursalin.queue_monitoring_system.controller;

import com.mursalin.queue_monitoring_system.dto.AppointmentRequest;
import com.mursalin.queue_monitoring_system.model.Appointment;
import com.mursalin.queue_monitoring_system.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }


    // Get all appointments for a doctor
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctor(
            @PathVariable String doctorId,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(appointmentService.findByDoctorId(doctorId, status));
    }

    // User checks in (updates status to WAITING)
    @PatchMapping("/{appointmentId}/check-in")
    public ResponseEntity<Appointment> checkIn(@PathVariable String appointmentId) {
        return ResponseEntity.ok(appointmentService.updateStatus(appointmentId, "WAITING"));
    }

    // Admin marks appointment as started
    @PatchMapping("/{appointmentId}/start")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Appointment> startAppointment(@PathVariable String appointmentId) {
        return ResponseEntity.ok(appointmentService.updateStatus(appointmentId, "IN_SESSION"));
    }

    // Admin marks appointment as completed
    @PatchMapping("/{appointmentId}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Appointment> completeAppointment(@PathVariable String appointmentId) {
        return ResponseEntity.ok(appointmentService.updateStatus(appointmentId, "COMPLETED"));
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@NonNull @RequestBody AppointmentRequest appointmentRequest) {
        return appointmentService.createAppointment(appointmentRequest);
    }

    @DeleteMapping("/cancel-appointment/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(@PathVariable String appointmentId) {
        return appointmentService.cancelAppointment(appointmentId);
    }
}
