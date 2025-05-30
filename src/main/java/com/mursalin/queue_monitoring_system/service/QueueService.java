package com.mursalin.queue_monitoring_system.service;

import com.mursalin.queue_monitoring_system.model.Appointment;
import com.mursalin.queue_monitoring_system.model.QueueLog;
import com.mursalin.queue_monitoring_system.repository.AppointmentRepository;
import com.mursalin.queue_monitoring_system.repository.QueueLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final AppointmentRepository appointmentRepository;
    private final QueueLogRepository queueLogRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // Get current queue status for a doctor
    public QueueLog getCurrentQueueStatus(String doctorId) {
        return queueLogRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new RuntimeException("No queue found for doctor ID: " + doctorId));
    }

    // Update the current serial number being served
    @Transactional
    public QueueLog updateCurrentSerial(String doctorId, long newSerial) {
        // Get existing queue log or create new one
        QueueLog queueLog = queueLogRepository.findByDoctorId(doctorId)
                .orElse(new QueueLog(null, doctorId, 0, false, LocalDateTime.now()));

        // Update the serial number
        queueLog.setCurrentSerial(newSerial);
        queueLog.setLastUpdated(LocalDateTime.now());

        // Check if doctor is running late
//        checkForDelays(queueLog, doctorId);

        // Save and broadcast update
        QueueLog savedLog = queueLogRepository.save(queueLog);
        broadcastQueueUpdate(savedLog);

        return savedLog;
    }

    // Get upcoming appointments for a doctor
    public List<Appointment> getUpcomingAppointments(String doctorId) {
        return appointmentRepository.findByDoctorIdAndStatus(doctorId, "WAITING");
    }

    // Calculate estimated wait time for a user
    public int calculateWaitTime(String doctorId, long serialNumber) {
        QueueLog queueLog = getCurrentQueueStatus(doctorId);
        List<Appointment> appointments = getUpcomingAppointments(doctorId);

        long usersAhead = serialNumber - queueLog.getCurrentSerial();
        if (usersAhead <= 0) return 0; // User's turn has come

        // Calculate average time per user (in minutes)
        long avgTimePerUser = calculateAverageConsultationTime(doctorId);

        return (int) (usersAhead * avgTimePerUser);
    }

    // Check if doctor is running behind schedule
//    private void checkForDelays(QueueLog queueLog, String doctorId) {
//        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndStatus(doctorId, "COMPLETED");
//
//        if (appointments.size() < 3) {
//            queueLog.setDelayed(false);
//            return;
//        }
//
//        // Get the last 3 completed appointments
//        List<Appointment> recentAppointments = appointments.subList(Math.max(appointments.size() - 3, 0), appointments.size());
//
//        // Calculate expected vs actual time
//        long totalDelay = 0;
//        for (Appointment appt : recentAppointments) {
//            long expectedDuration = 15; // Standard 15 minute slot
//            long actualDuration = ChronoUnit.MINUTES.between(
//                    appt.getSessionStartTime(),
//                    appt.getCompletionTime() != null ? appt.getCompletionTime() : LocalDateTime.now()
//            );
//            totalDelay += Math.max(0, actualDuration - expectedDuration);
//        }
//
//        // If average delay > 10 minutes, mark as delayed

//        long totalDelay =
//        if (totalDelay / recentAppointments.size() > 10) {
//            queueLog.setDelayed(true);
//            queueLog.setDelayReason("Doctor is running " + (totalDelay / recentAppointments.size()) + " minutes behind schedule");
//        } else {
//            queueLog.setDelayed(false);
//            queueLog.setDelayReason(null);
//        }
//}

    // Calculate average consultation time
    private long calculateAverageConsultationTime(String doctorId) {
        List<Appointment> completedAppointments = appointmentRepository.findByDoctorIdAndStatus(doctorId, "COMPLETED");

        if (completedAppointments.isEmpty()) {
            return 15; // Default 15 minutes if no data
        }

        long totalMinutes = 0;
        for (Appointment appt : completedAppointments) {
            if (appt.getCompletionTime() != null) {
                totalMinutes += ChronoUnit.MINUTES.between(
                        appt.getSessionStartTime(),
                        appt.getCompletionTime()
                );
            }
        }

        return totalMinutes / completedAppointments.size();
    }

    // Broadcast queue update to all subscribed clients
    private void broadcastQueueUpdate(QueueLog queueLog) {
        messagingTemplate.convertAndSend(
                "/topic/queue-updates/" + queueLog.getDoctorId(),
                queueLog
        );

        // Also broadcast to admin dashboard
        messagingTemplate.convertAndSend(
                "/topic/admin/queue-updates",
                queueLog
        );
    }

    // Get current position of a user in queue
    public long getUserPosition(String doctorId, String userId) {
        Optional<Appointment> appointment = appointmentRepository.findByDoctorIdAndUserId(doctorId, userId);
        return appointment.map(Appointment::getSerialNumber)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }
}

