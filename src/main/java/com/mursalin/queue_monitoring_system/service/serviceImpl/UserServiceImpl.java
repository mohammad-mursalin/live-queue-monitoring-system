package com.mursalin.queue_monitoring_system.service.serviceImpl;

import com.mursalin.queue_monitoring_system.dto.UserDto;
import com.mursalin.queue_monitoring_system.model.User;
import com.mursalin.queue_monitoring_system.model.UserRole;
import com.mursalin.queue_monitoring_system.repository.UserRepository;
import com.mursalin.queue_monitoring_system.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> registerUser(@NonNull UserDto userDto) {
        if(!userRepository.existByEmailAndPhoneIgnoreCase(userDto.getEmail(), userDto.getPhone())) {
            User user = User.builder()
                    .name(userDto.getName())
                    .phone(userDto.getPhone())
                    .email(userDto.getEmail())
                    .role(UserRole.ROLE_USER)
                    .build();
            User savedUser = userRepository.save(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("User Already Exist", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity userLogin(@NonNull UserDto userDto) {
        return null;
    }
}
