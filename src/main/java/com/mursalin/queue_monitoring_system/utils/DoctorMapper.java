package com.mursalin.queue_monitoring_system.utils;

import com.mursalin.queue_monitoring_system.dto.DoctorDto;
import com.mursalin.queue_monitoring_system.model.Doctor;

public class DoctorMapper {
    public static DoctorDto toDto(Doctor doctor) {
        return DoctorDto.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .specialty(doctor.getSpecialty())
                .schedule(doctor.getSchedule())
                .build();
    }
}

