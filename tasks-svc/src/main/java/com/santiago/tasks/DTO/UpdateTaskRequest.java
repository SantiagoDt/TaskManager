package com.santiago.tasks.DTO;

import lombok.Data;
import java.time.Instant;
import java.util.List;
import com.santiago.tasks.domain.Task;

@Data
public class UpdateTaskRequest {
    private String title;              // opcional
    private String description;        // opcional
    private List<String> tags;         // opcional
    private Task.Priority priority;    // opcional
    private Instant dueAt;             // opcional
    private Integer estimatedMinutes;  // opcional
    private Task.Status status;        // opcional
}
