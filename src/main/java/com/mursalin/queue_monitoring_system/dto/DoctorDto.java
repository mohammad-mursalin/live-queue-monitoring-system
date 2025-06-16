package com.mursalin.queue_monitoring_system.dto;

import com.mursalin.queue_monitoring_system.model.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {

    private String id;
    private String name;
    private String specialty;
    private Schedule schedule;
}
