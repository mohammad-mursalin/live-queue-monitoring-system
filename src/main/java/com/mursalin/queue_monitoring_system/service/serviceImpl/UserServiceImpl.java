package com.mursalin.queue_monitoring_system.service.serviceImpl;

import com.mursalin.queue_monitoring_system.dto.UserDto;
import com.mursalin.queue_monitoring_system.jwt.JwtService;
import com.mursalin.queue_monitoring_system.model.User;
import com.mursalin.queue_monitoring_system.model.UserRole;
import com.mursalin.queue_monitoring_system.repository.UserRepository;
import com.mursalin.queue_monitoring_system.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public ResponseEntity<?> registerUser(@NonNull UserDto userDto) {
        if(!userRepository.existByEmailAndPhoneIgnoreCase(userDto.getEmail(), userDto.getPhone())) {
            User user = User.builder()
                    .name(userDto.getName())
                    .phone(userDto.getPhone())
                    .email(userDto.getEmail())
                    .password(encoder.encode(userDto.getPassword()))
                    .role(UserRole.ROLE_USER)
                    .build();
            User savedUser = userRepository.save(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("User Already Exist", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity userLogin(@NonNull UserDto userDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));

            User authenticatedUser = userRepository.findByEmailAndPhoneIgnoreCase(userDto.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));


            if (authentication.isAuthenticated()) {
                String jwt = jwtService.generateToken(authenticatedUser);
                return new ResponseEntity<>(jwt, HttpStatus.OK);
            }
            throw new RuntimeException("Login failed due to invalid credentials.");


        } catch (AuthenticationException ex) {
            throw new RuntimeException("Invalid email or password.");
        }
    }
}
