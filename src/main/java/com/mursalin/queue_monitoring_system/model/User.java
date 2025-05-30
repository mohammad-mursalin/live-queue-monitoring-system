package com.mursalin.queue_monitoring_system.model;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
@Builder
public class User {
    @Id
    private String id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String confirmationCode;
    private UserRole role;
}
