package com.mursalin.queue_monitoring_system.service;

import com.mursalin.queue_monitoring_system.dto.UserDto;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> registerUser(@NonNull UserDto userDto);

    ResponseEntity userLogin(@NonNull UserDto userDto);
}
