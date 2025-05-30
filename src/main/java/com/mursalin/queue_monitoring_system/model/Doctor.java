package com.mursalin.queue_monitoring_system.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;

@Document(collection = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    @Id
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String specialty;
    @NonNull
    private Schedule schedule;
    private LocalTime delay;
}
