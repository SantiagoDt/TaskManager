package com.santiago.tasks.service;

import com.santiago.tasks.DTO.CreateTaskRequest;
import com.santiago.tasks.DTO.UpdateTaskRequest;
import com.santiago.tasks.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.santiago.tasks.domain.Task;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Collections;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor

public class TaskService {

    private final TaskRepository taskRepository;

    public Task createTask(String userId, CreateTaskRequest createRequest) {
        Task task = Task.builder()
                .title(createRequest.getTitle().trim())
                .description(createRequest.getDescription())
                .tags(createRequest.getTags() != null ? createRequest.getTags() : Collections.emptyList())
                .priority(createRequest.getPriority() != null ? createRequest.getPriority() : Task.Priority.LOW)
                .dueAt(createRequest.getDueAt())
                .estimatedMinutes(createRequest.getEstimatedMinutes())
                .userId(userId)
                .status(Task.Status.PENDING)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return taskRepository.save(task);
    }

    public void deleteTask(String userId, String taskId) {
        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new NoSuchElementException("Task not found or unauthorized"));
        taskRepository.delete(task);
    }

    public Task getTask(String userId, String taskId) {
        return taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new NoSuchElementException("Task not found or unauthorized"));
    }

    public Page<Task> getTasks(String userId, Pageable pageable) {
        return taskRepository.findByUserId(userId, pageable);
    }


    public Task updateTask(String userId, String taskId, UpdateTaskRequest updateRequest) {
        Task task = taskRepository.findByIdAndUserId(taskId, userId).orElseThrow(() -> new NoSuchElementException("Task not found or unauthorized"));
        if (updateRequest.getTitle() != null) {
            task.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getDescription() != null) {
            task.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getTags() != null) {
            task.setTags(updateRequest.getTags());
        }
        if (updateRequest.getPriority() != null) {
            task.setPriority(updateRequest.getPriority());
        }
        if (updateRequest.getDueAt() != null) {
            task.setDueAt(updateRequest.getDueAt());
        }
        if (updateRequest.getEstimatedMinutes() != null) {
            task.setEstimatedMinutes(updateRequest.getEstimatedMinutes());
        }
        if (updateRequest.getStatus() != null) {
            Task.Status oldStatus = task.getStatus();
            Task.Status newStatus = updateRequest.getStatus();

            if (oldStatus != Task.Status.DONE && newStatus == Task.Status.DONE) {
                task.setFinishedAt(Instant.now());
            }

            else if (oldStatus == Task.Status.DONE && newStatus != Task.Status.DONE) {
                task.setFinishedAt(null);
            }

            task.setStatus(newStatus);
        }
        task.setUpdatedAt(Instant.now());
        return taskRepository.save(task);
    }


}
