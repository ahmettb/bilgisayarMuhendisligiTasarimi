package com.jobtrackingapp.software_service.controller;

import com.jobtrackingapp.software_service.model.request.CreateTaskRequest;
import com.jobtrackingapp.software_service.model.request.UpdateTaskRequest;
import com.jobtrackingapp.software_service.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/task/")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @PostMapping("create")
    public ResponseEntity<?>createTask(@RequestBody CreateTaskRequest request)

    {
        taskService.createTask(request);
        return  new ResponseEntity<>(HttpStatus.OK);

    }
    @PutMapping("update-status")
    public ResponseEntity<?>updateTaskStatus(@RequestBody UpdateTaskRequest request)

    {
        taskService.updateTaskStatus(request);
        return  new ResponseEntity<>(HttpStatus.OK);

    }

}
