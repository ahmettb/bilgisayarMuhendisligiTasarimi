package com.jobtrackingapp.software_service.service;

import com.jobtrackingapp.software_service.model.Task;
import com.jobtrackingapp.software_service.model.TaskPriority;
import com.jobtrackingapp.software_service.model.TaskStatus;
import com.jobtrackingapp.software_service.model.User;
import com.jobtrackingapp.software_service.model.request.CreateTaskRequest;
import com.jobtrackingapp.software_service.model.request.UpdateTaskRequest;
import com.jobtrackingapp.software_service.model.response.ApiResponse;
import com.jobtrackingapp.software_service.model.response.TaskInfoResponse;
import com.jobtrackingapp.software_service.repository.TaskRepository;
import com.jobtrackingapp.software_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class TaskService {


    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    public ApiResponse<Void> createTask(CreateTaskRequest createTaskRequest) {

        try {
            User assignee = userRepository.findById(createTaskRequest.getAssigneeId()).orElseThrow(() -> new EntityNotFoundException());
            User assigneer = userRepository.findById(createTaskRequest.getAssigneerId()).orElseThrow(() -> new EntityNotFoundException());

            TaskStatus taskStatus = TaskStatus.valueOf(createTaskRequest.getStatus());
            TaskPriority taskPriority = TaskPriority.valueOf(createTaskRequest.getPriority());
            Task task = new Task();
            task.setPriority(taskPriority);
            task.setStatus(taskStatus);
            task.setCreateTime(new Date());
            task.setDescription(createTaskRequest.getDescription());
            task.setTitle(createTaskRequest.getTitle());
            task.setDeadLine(createTaskRequest.getDeadLine());
            task.setAssignee(assignee);
            task.setAssigner(assigneer);

            taskRepository.save(task);
            return ApiResponse.success("Task Created", null);
        } catch (Exception exception) {
            return ApiResponse.error(exception.getMessage());

        }


    }

    public ApiResponse<Void> updateTaskStatus(UpdateTaskRequest updateTaskRequest) {

        try {
            Task task = taskRepository.findById(updateTaskRequest.getTaskId()).orElseThrow(() -> new EntityNotFoundException());
            TaskStatus taskStatus = TaskStatus.valueOf(updateTaskRequest.getStatus());
            task.setStatus(taskStatus);
            taskRepository.save(task);
            return ApiResponse.success("Task Status Updated", null);

        } catch (Exception exception) {
            return ApiResponse.error(exception.getMessage());

        }

    }

    public ApiResponse<Void> deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));
        task.setDeleted(true);
        taskRepository.save(task);
        return ApiResponse.success("Task deleted successfully", null);
    }

    public ApiResponse<Void> updateTask(Long taskId, CreateTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

        User assignee = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new EntityNotFoundException("Assignee not found with ID: " + request.getAssigneeId()));
        User assigner = userRepository.findById(request.getAssigneerId())
                .orElseThrow(() -> new EntityNotFoundException("Assigner not found with ID: " + request.getAssigneerId()));

        TaskStatus taskStatus = TaskStatus.valueOf(request.getStatus());
        TaskPriority taskPriority = TaskPriority.valueOf(request.getPriority());
        task.setPriority(taskPriority);
        task.setStatus(taskStatus);

        task.setCreateTime(new Date());
        task.setDescription(request.getDescription());
        task.setTitle(request.getTitle());
        task.setDeadLine(request.getDeadLine());
        task.setAssignee(assignee);
        task.setAssigner(assigner);

        taskRepository.save(task);
        return ApiResponse.success("Task updated successfully", null);
    }

    public ApiResponse<TaskInfoResponse> getTaskInfo(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

        User assignee = userRepository.findById(task.getAssignee().getId()).get();
        User assigner = userRepository.findById(task.getAssigner().getId()).get();


        return ApiResponse.success("Get Info Task Success", new TaskInfoResponse(
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getPriority().name(),
                task.getDeadLine(),
                task.getCreateTime(),
                assigner.getId(),
                assigner.getName(),
                assigner.getSurname(),
                assignee.getId(),
                assignee.getName(),
                assignee.getSurname()


        ));

    }

}
