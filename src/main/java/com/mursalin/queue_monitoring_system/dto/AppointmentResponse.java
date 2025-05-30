package com.mursalin.queue_monitoring_system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppointmentResponse {

    private String password;
    private long serialNumber;
}
