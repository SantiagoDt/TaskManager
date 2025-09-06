package com.santiago.tasks.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.santiago.tasks.domain.Task;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task, String> {
    Optional<Task> findByIdAndUserId(String id, String userId);
    Page<Task> findByUserId(String userId, Pageable pageable);
}
