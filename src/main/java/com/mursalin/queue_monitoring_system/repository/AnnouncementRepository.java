package com.mursalin.queue_monitoring_system.repository;

import com.mursalin.queue_monitoring_system.model.Announcement;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AnnouncementRepository extends MongoRepository<Announcement, String> {
    List<Announcement> findTopByOrderByCreatedAtDesc(int limit);
}
