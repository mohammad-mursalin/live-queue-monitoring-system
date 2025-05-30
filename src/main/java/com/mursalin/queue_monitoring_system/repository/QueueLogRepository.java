package com.mursalin.queue_monitoring_system.repository;

import com.mursalin.queue_monitoring_system.model.QueueLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QueueLogRepository extends MongoRepository<QueueLog, String> {
    Optional<QueueLog> findByDoctorId(String doctorId);
}
