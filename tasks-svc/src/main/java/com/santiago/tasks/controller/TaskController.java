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


@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public Task createTask(
            HttpServletRequest request,
            @Valid @RequestBody CreateTaskRequest req) {
        String userId = request.getAttribute("userId").toString();
        return taskService.createTask(userId, req);
    }

    @GetMapping("/{taskId}")
    public Task getTask(@RequestHeader("X-USER-ID") String userId, @PathVariable String taskId) {
        return taskService.getTask(userId, taskId);
    }

    @GetMapping
    public Page<Task> getTasks(
            @RequestHeader("X-USER-ID") String userId,
            Pageable pageable,
            @RequestParam(required = false) Task.Status status,
            @RequestParam(required = false) Task.Priority priority,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdTo
    ) {
        return taskService.getTasks(userId, pageable, status, priority, tags, createdFrom, createdTo);
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(@RequestHeader("X-USER-ID") String userId, @PathVariable String taskId) {
        taskService.deleteTask(userId, taskId);
    }

    @PatchMapping("/{taskId}")
    public Task updateTask(@RequestHeader("X-USER-ID") String userId, @PathVariable String taskId, @RequestBody UpdateTaskRequest req) {
        return taskService.updateTask(userId, taskId, req);
    }


}
