package com.jobtrackingapp.software_service.controller;

import com.jobtrackingapp.software_service.model.request.CreatePermissionRequest;
import com.jobtrackingapp.software_service.model.request.CreateTaskRequest;
import com.jobtrackingapp.software_service.model.request.UpdateTaskRequest;
import com.jobtrackingapp.software_service.model.response.ApiResponse;
import com.jobtrackingapp.software_service.model.response.PermissionResponse;
import com.jobtrackingapp.software_service.model.response.TaskInfoResponse;
import com.jobtrackingapp.software_service.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/task/")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @PostMapping("add-permission")
    public ResponseEntity<ApiResponse<String>> createPermission(@RequestBody CreatePermissionRequest request)
    {
        return new ResponseEntity<>(taskService.createPermission(request), HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<ApiResponse<Void>> createTask(@RequestBody CreateTaskRequest request) {
        return new ResponseEntity<>(taskService.createTask(request), HttpStatus.OK);

    }


    @PutMapping("update-status")
    public ResponseEntity<ApiResponse<Void>> updateTaskStatus(@RequestBody UpdateTaskRequest request) {
        return new ResponseEntity<>(taskService.updateTaskStatus(request), HttpStatus.OK);

    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<Void>> updateTaskStatus(@PathVariable("id") Long id) {
        return new ResponseEntity<>(taskService.deleteTask(id), HttpStatus.OK);

    }

    @GetMapping("get-permission/{id}")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>
            >getPermission(@PathVariable("id") Long id) {
        return new ResponseEntity<>(taskService.getPermission(id), HttpStatus.OK);

    }

    @GetMapping("get/{id}")
    public ResponseEntity<ApiResponse<TaskInfoResponse>> getTaskInfo(@PathVariable("id") Long id) {
        return new ResponseEntity<>(taskService.getTaskInfo(id), HttpStatus.OK);

    }
    @GetMapping("get-by-software-id/{id}")
    public ResponseEntity<ApiResponse<List<TaskInfoResponse>>> getTaskInfoListBuSoftwareUserId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(taskService.getTaskInfoListBuSoftwareUserId(id), HttpStatus.OK);

    }





    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<Void>> updateTaskStatus(@RequestBody CreateTaskRequest request, @PathVariable("id") Long id) {
        return new ResponseEntity<>(taskService.updateTask(id, request), HttpStatus.OK);
    }
}
