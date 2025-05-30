package com.mursalin.queue_monitoring_system.service;

import com.mursalin.queue_monitoring_system.dto.AppointmentRequest;
import com.mursalin.queue_monitoring_system.dto.AppointmentResponse;
import com.mursalin.queue_monitoring_system.model.Appointment;
import com.mursalin.queue_monitoring_system.model.AppointmentStatus;
import com.mursalin.queue_monitoring_system.model.User;
import com.mursalin.queue_monitoring_system.model.UserRole;
import com.mursalin.queue_monitoring_system.repository.AppointmentRepository;
import com.mursalin.queue_monitoring_system.repository.UserRepository;
import com.mursalin.queue_monitoring_system.service.serviceImpl.MongodbTemplate;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final QueueService queueService;
    private final MongodbTemplate mongodbTemplate;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> createAppointment(@NonNull AppointmentRequest appointmentRequest) {
        Appointment appointment = Appointment.builder()
                .userId(appointmentRequest.getUserId())
                .doctorId(appointmentRequest.getDoctorId())
                .appointmentDate(appointmentRequest.getAppointmentDate())
                .serialNumber(mongodbTemplate.countTotalAppointment(appointmentRequest.getAppointmentDate(), appointmentRequest.getDoctorId())+1)
                .status(AppointmentStatus.SCHEDULED).build();

        Appointment saved = appointmentRepository.save(appointment);

        User user = userRepository.findById(appointmentRequest.getUserId()).get();
        user.setRole(UserRole.ROLE_PATIENT);
        user.setPassword(user.getId() + UUID.randomUUID().toString().replace("-", ""));

        userRepository.save(user);

        AppointmentResponse appointmentResponse = AppointmentResponse.builder()
                .password(user.getPassword())
                .serialNumber(appointment.getSerialNumber())
                .build();

        // Broadcast to admin dashboard
        messagingTemplate.convertAndSend("/topic/admin/new-appointments", saved);
        return new ResponseEntity<>(appointmentResponse, HttpStatus.CREATED);
    }

    @Transactional
    public Appointment updateStatus(String appointmentId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        switch (status) {
            case "WAITING":
                appointment.setStatus(AppointmentStatus.WAITING);
                break;
            case "IN_SESSION":
                appointment.setStatus(AppointmentStatus.IN_SESSION);
                // Notify next user
                messagingTemplate.convertAndSend(
                        "/topic/queue-updates/" + appointment.getDoctorId(),
                        "Doctor is now seeing serial: " + appointment.getSerialNumber()
                );
                break;
            case "COMPLETED":
                appointment.setStatus(AppointmentStatus.COMPLETED);
                User user = userRepository.findById(appointment.getUserId()).get();
                user.setPassword(null);
                user.setRole(UserRole.ROLE_USER);
                userRepository.save(user);
                // Auto-advance queue
                queueService.updateCurrentSerial(
                        appointment.getDoctorId(),
                        appointment.getSerialNumber() + 1
                );
                break;
            default:
                throw new IllegalArgumentException("Invalid status transition");
        }

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> findByDoctorId(String doctorId, String status) {
        if (status != null) {
            return appointmentRepository.findByDoctorIdAndStatus(doctorId, status);
        }
        return appointmentRepository.findByDoctorId(doctorId);
    }

    @Transactional
    public void recalculateAllWaitTimes(String doctorId) {
        List<Appointment> waitingAppointments = appointmentRepository
                .findByDoctorIdAndStatus(doctorId, AppointmentStatus.WAITING.name());

        waitingAppointments.forEach(appt -> {
            int waitTime = queueService.calculateWaitTime(
                    doctorId,
                    appt.getSerialNumber()
            );
            appt.setEstimatedWaitMinutes(waitTime);
        });

        appointmentRepository.saveAll(waitingAppointments);
    }


}

