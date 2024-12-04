package com.jobtrackingapp.software_service.service;

import com.jobtrackingapp.software_service.model.Task;
import com.jobtrackingapp.software_service.model.TaskPriority;
import com.jobtrackingapp.software_service.model.TaskStatus;
import com.jobtrackingapp.software_service.model.request.CreateTaskRequest;
import com.jobtrackingapp.software_service.model.request.UpdateTaskRequest;
import com.jobtrackingapp.software_service.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class TaskService {


    private final TaskRepository taskRepository;

    public void createTask(CreateTaskRequest createTaskRequest)
    {

        TaskStatus taskStatus=TaskStatus.valueOf(createTaskRequest.getStatus());
        TaskPriority taskPriority=TaskPriority.valueOf(createTaskRequest.getPriority());
        Task task=new Task();
        task.setPriority(taskPriority);
        task.setStatus(taskStatus);
        task.setCreateTime(new Date());
        task.setDescription(createTaskRequest.getDescription());
        task.setTitle(createTaskRequest.getTitle());
        task.setDeadLine(createTaskRequest.getDeadLine());


        taskRepository.save(task);


    }

    public void updateTaskStatus(UpdateTaskRequest updateTaskRequest){

       Task task= taskRepository.findById(updateTaskRequest.getTaskId()).orElseThrow(()->new EntityNotFoundException());
       TaskStatus taskStatus=TaskStatus.valueOf(updateTaskRequest.getStatus());
        task.setStatus(taskStatus);
        taskRepository.save(task);
    }

}
