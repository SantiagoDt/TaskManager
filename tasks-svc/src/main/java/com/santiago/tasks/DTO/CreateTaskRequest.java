package com.santiago.tasks.DTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;
import com.santiago.tasks.domain.Task.Priority;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTaskRequest {
    private String title; // obligatorio
    private String description; // opcional
    private List<String> tags; // opcional
    private Priority priority; // opcional
    private Instant dueAt; // opcional
    private Integer estimatedMinutes; // opcional
}