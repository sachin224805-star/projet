package com.weconnect.repository;

import com.weconnect.model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends MongoRepository<Report, String> {
    List<Report> findByUserIdOrderByCreatedAtDesc(String userId);
}
