package com.mursalin.queue_monitoring_system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String password;

}
