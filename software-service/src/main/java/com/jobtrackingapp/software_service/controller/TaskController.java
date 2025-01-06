package com.jobtrackingapp.software_service.controller;

import com.jobtrackingapp.software_service.model.request.CreatePermissionRequest;
import com.jobtrackingapp.software_service.model.request.CreateTaskRequest;
import com.jobtrackingapp.software_service.model.request.UpdateTaskRequest;
import com.jobtrackingapp.software_service.model.response.*;
import com.jobtrackingapp.software_service.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/task/")
@AllArgsConstructor
@Log4j2
public class TaskController {

    private final TaskService taskService;



    @PostMapping("create")
    public ResponseEntity<ApiResponse<Void>> createTask(@RequestBody CreateTaskRequest request) {
        log.info("Creating task with title: {}", request.getTitle()); // Loglama
        ApiResponse<Void> response = taskService.createTask(request);
        if (response.isSuccess()) {
            log.info("Task successfully created with title: {}", request.getTitle()); // Loglama
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            log.error("Failed to create task with title: {}", request.getTitle()); // Loglama
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("update-status")
    public ResponseEntity<ApiResponse<Void>> updateTaskStatus(@RequestBody UpdateTaskRequest request) {
        log.info("Updating task status for task ID: {}", request.getTaskId()); // Loglama
        ApiResponse<Void> response = taskService.updateTaskStatus(request);
        if (response.isSuccess()) {
            log.info("Task status updated successfully for task ID: {}", request.getTaskId()); // Loglama
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.error("Failed to update task status for task ID: {}", request.getTaskId()); // Loglama
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable("id") Long id) {
        log.info("Deleting task with ID: {}", id); // Loglama
        ApiResponse<Void> response = taskService.deleteTask(id);
        if (response.isSuccess()) {
            log.info("Task deleted successfully with ID: {}", id); // Loglama
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT); // No content for delete
        } else {
            log.error("Failed to delete task with ID: {}", id); // Loglama
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("get/{id}")
    public ResponseEntity<ApiResponse<TaskInfoResponse>> getTaskInfo(@PathVariable("id") Long id) {
        log.info("Fetching task info for task ID: {}", id); // Loglama
        ApiResponse<TaskInfoResponse> response = taskService.getTaskInfo(id);
        if (response.isSuccess()) {
            log.info("Task info fetched successfully for task ID: {}", id); // Loglama
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.error("Failed to fetch task info for task ID: {}", id); // Loglama
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

        @GetMapping("get-tasks-by-user-and-date")
    public ResponseEntity<ApiResponse<TaskStatusChangeAnalysis>> getTasksByUserIdAndDate(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        log.info("Fetching tasks for user ID: {} between dates: {} and {}", userId, startDate, endDate);

        ApiResponse<TaskStatusChangeAnalysis> response = taskService.getTaskStatusChangeAnalysis(userId, startDate, endDate);

        if (response.isSuccess()) {
            log.info("Tasks fetched successfully for user ID: {} between dates: {} and {}", userId, startDate, endDate);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.error("Failed to fetch tasks for user ID: {} between dates: {} and {}", userId, startDate, endDate);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("get-task-status-analysis/{id}")
    public ResponseEntity<ApiResponse<TaskStatusAnalysis>> getTaskStatusAnalysis(
            @PathVariable(name = "id") Long userId) {

        log.info("Fetching overall task status analysis for user ID: {}", userId);

        ApiResponse<TaskStatusAnalysis> response = taskService.getTaskStatusAnalysis(userId);

        if (response.isSuccess()) {
            log.info("Task status analysis fetched successfully for user ID: {}", userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.error("Failed to fetch task status analysis for user ID: {}", userId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("get-task-completion-rate/{userId}")
    public ResponseEntity<ApiResponse<UserTaskCompletionRateAnalysis>> getUserTaskCompletionRate(
            @PathVariable("userId") Long userId) {

        log.info("Fetching task completion rate for user ID: {}", userId);

        ApiResponse<UserTaskCompletionRateAnalysis> response = taskService.getUserTaskCompletionRateAnalysis(userId);

        if (response.isSuccess()) {
            log.info("Task completion rate fetched successfully for user ID: {}", userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.error("Failed to fetch task completion rate for user ID: {}", userId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("get-task-by-status")
    public ResponseEntity<ApiResponse<List<TaskInfoResponse>>> getTaskInfo(@RequestParam(name = "userId") Long userId, @RequestParam(name = "status") String status) {
        ApiResponse<List<TaskInfoResponse>> response = taskService.getTasksByStatus(userId, status);
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("get-task-by-assignee/{id}")
    public ResponseEntity<ApiResponse<Page<TaskInfoResponse>>> getTaskInfoListBySoftwareUserId(
            @PathVariable("id") Long id, Pageable pageable) {

        ApiResponse<Page<TaskInfoResponse>> response = taskService.getTaskInfoListBySoftwareUserId(id, pageable);

        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<Void>> updateTask(@RequestBody CreateTaskRequest request, @PathVariable("id") Long id) {
        log.info("Updating task with ID: {}", id); // Loglama
        ApiResponse<Void> response = taskService.updateTask(id, request);
        if (response.isSuccess()) {
            log.info("Task updated successfully with ID: {}", id); // Loglama
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.error("Failed to update task with ID: {}", id); // Loglama
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}