package com.mursalin.queue_monitoring_system.repository;

import com.mursalin.queue_monitoring_system.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String > {
    boolean existByEmailAndPhoneIgnoreCase(String email, String phone);
}
