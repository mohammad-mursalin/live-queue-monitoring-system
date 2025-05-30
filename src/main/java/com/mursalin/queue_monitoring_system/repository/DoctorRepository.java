package com.mursalin.queue_monitoring_system.repository;

import com.mursalin.queue_monitoring_system.model.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoctorRepository extends MongoRepository<Doctor, String> {
}
