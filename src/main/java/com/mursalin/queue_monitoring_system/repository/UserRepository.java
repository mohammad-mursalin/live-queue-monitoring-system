package com.mursalin.queue_monitoring_system.repository;

import com.mursalin.queue_monitoring_system.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String > {
    boolean existByEmailAndPhoneIgnoreCase(String email, String phone);

    Optional<User> findByEmailAndPhoneIgnoreCase(String email);

    Optional<User> findByEmail(String email);
}
