package com.jobtrackingapp.software_service.controller;

import com.jobtrackingapp.software_service.model.request.CreatePermissionRequest;
import com.jobtrackingapp.software_service.model.request.CreateTaskRequest;
import com.jobtrackingapp.software_service.model.request.UpdateTaskRequest;
import com.jobtrackingapp.software_service.model.response.ApiResponse;
import com.jobtrackingapp.software_service.model.response.PermissionResponse;
import com.jobtrackingapp.software_service.model.response.TaskInfoResponse;
import com.jobtrackingapp.software_service.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/task/")
@AllArgsConstructor
@Log4j2 // Loglama için Lombok kullanılıyor
public class TaskController {

    private final TaskService taskService;

    // Permission ekleme işlemi
    @PostMapping("add-permission")
    public ResponseEntity<ApiResponse<String>> createPermission(@RequestBody CreatePermissionRequest request) {
        log.info("Creating permission for user with ID: {}", request.getUserId()); // Loglama
        ApiResponse<String> response = taskService.createPermission(request);
        if (response.isSuccess()) {
            log.info("Permission successfully created for user with ID: {}", request.getUserId()); // Loglama
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.error("Failed to create permission for user with ID: {}", request.getUserId()); // Loglama
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Yeni görev oluşturma işlemi
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

    // Görev durumu güncelleme işlemi
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

    // Görev silme işlemi
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

    // Kullanıcı izin bilgilerini alma işlemi
    @GetMapping("get-permission/{id}")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getPermission(@PathVariable("id") Long id) {
        log.info("Fetching permissions for user ID: {}", id); // Loglama
        ApiResponse<List<PermissionResponse>> response = taskService.getPermission(id);
        if (response.isSuccess()) {
            log.info("Permissions fetched successfully for user ID: {}", id); // Loglama
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.error("Failed to fetch permissions for user ID: {}", id); // Loglama
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // Görev bilgisi alma işlemi
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

    // Yazılım kullanıcı ID'ye göre görev bilgisi listeleme işlemi
    @GetMapping("get-by-software-id/{id}")
    public ResponseEntity<ApiResponse<List<TaskInfoResponse>>> getTaskInfoListBuSoftwareUserId(@PathVariable("id") Long id) {
        log.info("Fetching tasks for software user ID: {}", id); // Loglama
        ApiResponse<List<TaskInfoResponse>> response = taskService.getTaskInfoListBySoftwareUserId(id);
        if (response.isSuccess()) {
            log.info("Tasks fetched successfully for software user ID: {}", id); // Loglama
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.error("Failed to fetch tasks for software user ID: {}", id); // Loglama
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // Görev güncelleme işlemi
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