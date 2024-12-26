package com.jobtrackingapp.software_service.service;

import com.jobtrackingapp.software_service.model.*;
import com.jobtrackingapp.software_service.model.request.CreatePermissionRequest;
import com.jobtrackingapp.software_service.model.request.CreateTaskRequest;
import com.jobtrackingapp.software_service.model.request.UpdateTaskRequest;
import com.jobtrackingapp.software_service.model.response.ApiResponse;
import com.jobtrackingapp.software_service.model.response.PermissionResponse;
import com.jobtrackingapp.software_service.model.response.TaskInfoResponse;
import com.jobtrackingapp.software_service.repository.PermissionUserRepository;
import com.jobtrackingapp.software_service.repository.SoftwareUserRepository;
import com.jobtrackingapp.software_service.repository.TaskRepository;
import com.jobtrackingapp.software_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final SoftwareUserRepository softwareUserRepository;
    private final PermissionUserRepository permissionUserRepository;

    public ApiResponse<String> createPermission(CreatePermissionRequest request) {
        try {
            PermissionUser permissionUser = new PermissionUser();
            User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("User Not Found With ID" + request.getUserId()));

            permissionUser.setDayCount(request.getDayCount());
            permissionUser.setStartDateOfPermission(request.getStartDateOfPermission());
            permissionUser.setAccepted(false);
            permissionUser.setUser(user);
            permissionUserRepository.save(permissionUser);

            return ApiResponse.success("Permission Request Success", null);
        } catch (EntityNotFoundException ex) {
            return ApiResponse.error("User not found: " + ex.getMessage());
        } catch (Exception ex) {
            return ApiResponse.error("Error while creating permission: " + ex.getMessage());
        }
    }

    public ApiResponse<List<PermissionResponse>> getPermission(Long id) {
        try {
            List<PermissionUser> permissionUser = permissionUserRepository.findByUser_Id(id).orElseThrow(() -> new EntityNotFoundException("Permissions not found for user with ID: " + id));

            List<PermissionResponse> responseList = new ArrayList<>();
            for (PermissionUser permissionUser1 : permissionUser) {
                PermissionResponse permissionResponse = new PermissionResponse();
                permissionResponse.setSurname(permissionUser1.getUser().getSurname());
                permissionResponse.setUserName(permissionUser1.getUser().getName());
                permissionResponse.setUserId(permissionUser1.getUser().getId());
                permissionResponse.setStartDateOfPermission(permissionUser1.getStartDateOfPermission());
                permissionResponse.setDayCount(permissionUser1.getDayCount());
                permissionResponse.setAccepted(permissionUser1.getAccepted());
                permissionResponse.setPermissionId(permissionUser1.getId());
                responseList.add(permissionResponse);
            }

            return ApiResponse.success("Get Permission Success", responseList);
        } catch (EntityNotFoundException ex) {
            return ApiResponse.error("Permissions not found: " + ex.getMessage());
        } catch (Exception ex) {
            return ApiResponse.error("Error while fetching permissions: " + ex.getMessage());
        }
    }

    public ApiResponse<Void> createTask(CreateTaskRequest createTaskRequest) {
        try {
            User assignee = userRepository.findById(createTaskRequest.getAssigneeId()).orElseThrow(() -> new EntityNotFoundException("Assignee not found"));
            User assigneer = userRepository.findById(createTaskRequest.getAssigneerId()).orElseThrow(() -> new EntityNotFoundException("Assigner not found"));

            TaskStatus taskStatus = TaskStatus.valueOf(createTaskRequest.getStatus());
            TaskPriority taskPriority = TaskPriority.valueOf(createTaskRequest.getPriority());
            Task task = new Task();
            task.setPriority(taskPriority);
            task.setStatus(taskStatus);
            task.setCreateTime(new Date());
            task.setDescription(createTaskRequest.getDescription());
            task.setTitle(createTaskRequest.getTitle());
            task.setDeadLine(createTaskRequest.getDeadLine());

            SoftwareUser assignerSoftware = softwareUserRepository.findSoftwareUserByUserId(createTaskRequest.getAssigneerId())
                    .orElseGet(() -> {
                        SoftwareUser newAssignerSoftware = new SoftwareUser();
                        newAssignerSoftware.setUser(assigneer);
                        return softwareUserRepository.save(newAssignerSoftware);
                    });

            SoftwareUser assigneSoftware = softwareUserRepository.findSoftwareUserByUserId(createTaskRequest.getAssigneeId())
                    .orElseGet(() -> {
                        SoftwareUser newAssigneSoftware = new SoftwareUser();
                        newAssigneSoftware.setUser(assignee);
                        return softwareUserRepository.save(newAssigneSoftware);
                    });

            task.setAssignee(assigneSoftware);
            task.setAssigner(assignerSoftware);
            taskRepository.save(task);

            return ApiResponse.success("Task Created", null);
        } catch (EntityNotFoundException ex) {
            return ApiResponse.error("Error: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ApiResponse.error("Invalid task status or priority: " + ex.getMessage());
        } catch (Exception ex) {
            return ApiResponse.error("Error while creating task: " + ex.getMessage());
        }
    }

    public ApiResponse<Void> updateTaskStatus(UpdateTaskRequest updateTaskRequest) {
        try {
            Task task = taskRepository.findByIdAndDeletedFalse(updateTaskRequest.getTaskId()).orElseThrow(() -> new EntityNotFoundException("Task not found"));
            TaskStatus taskStatus = TaskStatus.valueOf(updateTaskRequest.getStatus());
            task.setStatus(taskStatus);
            taskRepository.save(task);
            return ApiResponse.success("Task Status Updated", null);
        } catch (EntityNotFoundException ex) {
            return ApiResponse.error("Task not found: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ApiResponse.error("Invalid task status: " + ex.getMessage());
        } catch (Exception ex) {
            return ApiResponse.error("Error while updating task status: " + ex.getMessage());
        }
    }

    public ApiResponse<Void> deleteTask(Long taskId) {
        try {
            Task task = taskRepository.findByIdAndDeletedFalse(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));
            task.setDeleted(true);
            taskRepository.save(task);
            return ApiResponse.success("Task deleted successfully", null);
        } catch (EntityNotFoundException ex) {
            return ApiResponse.error("Task not found: " + ex.getMessage());
        } catch (Exception ex) {
            return ApiResponse.error("Error while deleting task: " + ex.getMessage());
        }
    }

    public ApiResponse<Void> updateTask(Long taskId, CreateTaskRequest request) {
        try {
            Task task = taskRepository.findByIdAndDeletedFalse(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("Assignee not found"));
            User assigner = userRepository.findById(request.getAssigneerId())
                    .orElseThrow(() -> new EntityNotFoundException("Assigner not found"));

            TaskStatus taskStatus = TaskStatus.valueOf(request.getStatus());
            TaskPriority taskPriority = TaskPriority.valueOf(request.getPriority());
            task.setPriority(taskPriority);
            task.setStatus(taskStatus);
            task.setCreateTime(new Date());
            task.setDescription(request.getDescription());
            task.setTitle(request.getTitle());
            task.setDeadLine(request.getDeadLine());

            taskRepository.save(task);
            return ApiResponse.success("Task updated successfully", null);
        } catch (EntityNotFoundException ex) {
            return ApiResponse.error("Error: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ApiResponse.error("Invalid task status or priority: " + ex.getMessage());
        } catch (Exception ex) {
            return ApiResponse.error("Error while updating task: " + ex.getMessage());
        }
    }

    public ApiResponse<List<TaskInfoResponse>> getTaskInfoListBySoftwareUserId(Long id) {
        try {
            SoftwareUser softwareUser = softwareUserRepository.findSoftwareUserByUserId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Software User not found with ID: " + id));

            List<TaskInfoResponse> taskInfoResponseList = new ArrayList<>();
            for (Task task : softwareUser.getTasksAssigned().stream().filter(task -> !task.isDeleted()).toList()) {
                taskInfoResponseList.add(
                        new TaskInfoResponse(
                                task.getTitle(),
                                task.getDescription(),
                                task.getStatus().name(),
                                task.getPriority().name(),
                                task.getDeadLine(),
                                task.getCreateTime(),
                                softwareUser.getUser().getId(),
                                softwareUser.getUser().getName(),
                                softwareUser.getUser().getSurname(),
                                softwareUser.getUser().getId(),
                                softwareUser.getUser().getName(),
                                softwareUser.getUser().getSurname()
                        )
                );
            }
            return ApiResponse.success("Get Task By Software User ID Success", taskInfoResponseList);
        } catch (EntityNotFoundException ex) {
            return ApiResponse.error("Software User not found: " + ex.getMessage());
        } catch (Exception ex) {
            return ApiResponse.error("Error while fetching tasks by software user: " + ex.getMessage());
        }
    }

    public ApiResponse<TaskInfoResponse> getTaskInfo(Long taskId) {
        try {
            Task task = taskRepository.findByIdAndDeletedFalse(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

            User assignee = userRepository.findById(task.getAssignee().getId()).get();
            User assigner = userRepository.findById(task.getAssigner().getId()).get();

            return ApiResponse.success("Get Task Info Success", new TaskInfoResponse(
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
        } catch (EntityNotFoundException ex) {
            return ApiResponse.error("Task not found: " + ex.getMessage());
        } catch (Exception ex) {
            return ApiResponse.error("Error while fetching task info: " + ex.getMessage());
        }
    }
}