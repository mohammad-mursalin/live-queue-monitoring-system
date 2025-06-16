package com.mursalin.queue_monitoring_system.service.serviceImpl;

import com.mursalin.queue_monitoring_system.model.User;
import com.mursalin.queue_monitoring_system.model.UserPrinciples;
import com.mursalin.queue_monitoring_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));

            return new UserPrinciples(user);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        try {
            User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));

            return new UserPrinciples(user);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
