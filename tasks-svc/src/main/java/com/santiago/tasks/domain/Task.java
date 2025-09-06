package com.santiago.tasks.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Task {
    @Id
    private String id;
    private String userId;
    private String title;
    private String description;
    private List<String> tags;
    private Priority priority;
    private Status status;
    private Integer position;
    private Instant dueAt;
    private Integer estimatedMinutes;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant finishedAt;

    public enum Priority { LOW, MEDIUM, HIGH }
    public enum Status { PENDING, IN_PROGRESS, DONE }

}
