package com.santiago.tasks.DTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;
import com.santiago.tasks.domain.Task.Priority;
import jakarta.validation.constraints.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTaskRequest {
    @NotBlank(message = "Title is mandatory")
    @Size(max = 20 , message = "Title can have at most 20 characters")
    private String title;
    @Size(max = 200, message = "Description can have at most 200 characters")
    private String description;

    private List<String> tags;
    private Priority priority;
    @FutureOrPresent(message = "dueAt must be in the present or future")
    private Instant dueAt;
    @Min(value = 1, message = "estimatedMinutes must be at least 1")
    @Max(value = 1440, message = "estimatedMinutes can be at most 1440")
    private Integer estimatedMinutes;
}