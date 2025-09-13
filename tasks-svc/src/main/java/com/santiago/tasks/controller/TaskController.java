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
            @Valid @RequestBody CreateTaskRequest taskRequest) {
        String userId = request.getAttribute("userId").toString();
        return taskService.createTask(userId, taskRequest);
    }

    @GetMapping("/{taskId}")
    public Task getTask(HttpServletRequest request, @PathVariable String taskId) {
        String userId = request.getAttribute("userId").toString();
        return taskService.getTask(userId, taskId);
    }

    @GetMapping
    public Page<Task> getTasks(HttpServletRequest request,
            Pageable pageable,
            @RequestParam(required = false) Task.Status status,
            @RequestParam(required = false) Task.Priority priority,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdTo
    ) {
        String userId = request.getAttribute("userId").toString();
        return taskService.getTasks(userId, pageable, status, priority, tags, createdFrom, createdTo);
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(HttpServletRequest request , @PathVariable String taskId) {
        String userId = request.getAttribute("userId").toString();
        taskService.deleteTask(userId, taskId);
    }

    @PatchMapping("/{taskId}")
    public Task updateTask(HttpServletRequest request, @PathVariable String taskId, @RequestBody UpdateTaskRequest req) {
        String userId = request.getAttribute("userId").toString();
        return taskService.updateTask(userId, taskId, req);
    }


}
