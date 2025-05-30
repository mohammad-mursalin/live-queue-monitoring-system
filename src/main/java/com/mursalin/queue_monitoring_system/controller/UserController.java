package com.mursalin.queue_monitoring_system.controller;

import com.mursalin.queue_monitoring_system.dto.UserDto;
import com.mursalin.queue_monitoring_system.service.DoctorService;
import com.mursalin.queue_monitoring_system.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final DoctorService doctorService;

    @PostMapping("/registration")
    public ResponseEntity<?> userRegistration(@NonNull @RequestBody UserDto userDto) {
        return userService.registerUser(userDto);
    }

    @GetMapping
    public ResponseEntity<?> userLogin(@RequestBody @NonNull UserDto userDto) {
        return userService.userLogin(userDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'PATIENT')")
    public ResponseEntity<?> getAllDoctors() {
        return doctorService.getAllDoctors();
    }
}
