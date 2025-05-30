package com.mursalin.queue_monitoring_system.repository;

import com.mursalin.queue_monitoring_system.model.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    List<Appointment> findByDoctorIdAndStatus(String doctorId, String status);
    Appointment findByDoctorIdAndSerialNumber(String doctorId, int serialNumber);

    List<Appointment> findByDoctorId(String doctorId);

    Optional<Appointment> findByDoctorIdAndUserId(String doctorId, String userId);
}
