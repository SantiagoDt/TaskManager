package com.santiago.tasks.controller;

import com.santiago.tasks.DTO.CreateTaskRequest;
import com.santiago.tasks.DTO.UpdateTaskRequest;
import com.santiago.tasks.domain.Task;
import com.santiago.tasks.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(
            HttpServletRequest request,
            @Valid @RequestBody CreateTaskRequest taskRequest) {
        String userId = request.getAttribute("userId").toString();
        Task task = taskService.createTask(userId, taskRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(task.getId())
                .toUri();

        return ResponseEntity.created(location).body(task);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(HttpServletRequest request, @PathVariable String taskId) {
        String userId = request.getAttribute("userId").toString();
        Task task = taskService.getTask(userId, taskId);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<Page<Task>> getTasks(HttpServletRequest request,
            Pageable pageable,
            @RequestParam(required = false) Task.Status status,
            @RequestParam(required = false) Task.Priority priority,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdTo
    ) {
        String userId = request.getAttribute("userId").toString();
        Page<Task> page = taskService.getTasks(userId, pageable, status, priority, tags, createdFrom, createdTo);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(HttpServletRequest request , @PathVariable String taskId) {
        String userId = request.getAttribute("userId").toString();
        taskService.deleteTask(userId, taskId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(HttpServletRequest request, @PathVariable String taskId, @RequestBody UpdateTaskRequest req) {
        String userId = request.getAttribute("userId").toString();
        Task updated = taskService.updateTask(userId, taskId, req);
        return ResponseEntity.ok(updated);
    }


}
