package com.mursalin.queue_monitoring_system.service.serviceImpl;

import com.mursalin.queue_monitoring_system.model.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;

@RequiredArgsConstructor
public class MongodbTemplate {

    private final MongoTemplate mongoTemplate;

    public long countTotalAppointment(LocalDate date, String doctorId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("doctorId").is(doctorId)
                .and("appointmentTime").is(date));
        return mongoTemplate.count(query, Appointment.class);
    }
}
