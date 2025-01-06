package com.jobtrackingapp.software_service.service;

import com.jobtrackingapp.software_service.model.*;
import com.jobtrackingapp.software_service.model.request.CreatePermissionRequest;
import com.jobtrackingapp.software_service.model.request.CreateTaskRequest;
import com.jobtrackingapp.software_service.model.request.UpdateTaskRequest;
import com.jobtrackingapp.software_service.model.response.*;
import com.jobtrackingapp.software_service.repository.PermissionUserRepository;
import com.jobtrackingapp.software_service.repository.SoftwareUserRepository;
import com.jobtrackingapp.software_service.repository.TaskRepository;
import com.jobtrackingapp.software_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final SoftwareUserRepository softwareUserRepository;
    private final PermissionUserRepository permissionUserRepository;




    public ApiResponse<UserTaskCompletionRateAnalysis> getUserTaskCompletionRateAnalysis(Long userId) {
        List<Task> tasks = taskRepository.findAll().stream()
                .filter(t -> !t.isDeleted() && t.getAssignee().getUser().getId()==userId)
                .toList();

        int totalTasks = tasks.size();
        int completedTasks = (int) tasks.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();

        double completionRate = totalTasks > 0 ? (double) completedTasks / totalTasks * 100 : 0;

        UserTaskCompletionRateAnalysis analysis = new UserTaskCompletionRateAnalysis(completionRate);
        return ApiResponse.success("User task completion rate analysis retrieved successfully", analysis);
    }
    public ApiResponse<TaskStatusChangeAnalysis> getTaskStatusChangeAnalysis(Long userId, Date startDate, Date endDate) {
        List<Task> tasks = taskRepository.findAll().stream()
                .filter(t -> t.isDeleted() == false
                        && t.getAssignee().getUser().getId() == userId
                        && t.getCreateTime().after(startDate)
                        && t.getCreateTime().before(endDate)
                ).toList();

        int newTasks = 0;
        int inProgressTasks = 0;
        int completedTasks = 0;

        for (Task task : tasks) {
            if (task.getStatus().name().equals("TO_DO")) {
                newTasks++;
            } else if (task.getStatus().name().equals("IN_PROGRESS")) {
                inProgressTasks++;
            } else if (task.getStatus().name().equals("COMPLETED")) {
                completedTasks++;
            }
        }

        TaskStatusChangeAnalysis analysis = new TaskStatusChangeAnalysis(newTasks, inProgressTasks, completedTasks);
        return ApiResponse.success("Task status change analysis retrieved successfully", analysis);
    }

    public ApiResponse<TaskStatusAnalysis> getTaskStatusAnalysis(Long userId) {
        List<Task> tasks = taskRepository.findAll().stream().filter(t-> t.isDeleted()==false && t.getAssignee().getUser().getId()==userId).toList();

        int toDo = 0;
        int inProgress = 0;
        int completed = 0;

        for (Task task : tasks) {
            switch (task.getStatus().name()) {
                case "TO_DO":
                    toDo++;
                    break;
                case "IN_PROGRESS":
                    inProgress++;
                    break;
                case "COMPLETED":
                    completed++;
                    break;
            }
        }

        TaskStatusAnalysis analysis = new TaskStatusAnalysis(toDo, inProgress, completed);
        return  ApiResponse.success("Task status analysis retrieved successfully", analysis);
    }

    private List<CommentResponse>convertToCommentResponse(Task task)
    {
        List<CommentResponse> commentResponseList = new ArrayList<>();

        if(task.getComments().isEmpty())
        {
            return  commentResponseList;
        }

            task.getComments().forEach(comment -> {
                CommentResponse commentResponse = new CommentResponse();
                commentResponse.setName(comment.getUser().getName());
                commentResponse.setSurname(comment.getUser().getSurname());
                commentResponse.setContent(comment.getContent());
                commentResponse.setCommentTime(comment.getCreatedAt());
                commentResponseList.add(commentResponse);
            });

        return commentResponseList;

    }

    public ApiResponse<List<TaskInfoResponse>> getTasksByStatus(Long userId, String status) {
        try {
            TaskStatus taskStatus;
            try {
                taskStatus = TaskStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException ex) {
                return ApiResponse.error("Invalid task status: " + status);
            }

            List<TaskInfoResponse> responseList = new ArrayList<>();
            List<Task> taskList = taskRepository.findAll().stream().filter(task -> task.getStatus().name().equals(status) && task.getAssignee().getId() == userId).toList();

            for (Task task : taskList) {

                List<CommentResponse>commentResponseList=convertToCommentResponse(task);

                TaskInfoResponse response = new TaskInfoResponse(
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus().name(),
                        task.getPriority().name(),
                        task.getDeadLine(),
                        task.getCreateTime(),
                        task.getAssignee().getId(),
                        task.getAssignee().getUser().getName(),
                        task.getAssignee().getUser().getSurname(),
                        task.getAssigner().getId(),
                        task.getAssigner().getUser().getName(),
                        task.getAssigner().getUser().getSurname(),
                        commentResponseList

                );
                responseList.add(response);
            }


            return ApiResponse.success("Get Tasks By Status Success", responseList);
        } catch (Exception ex) {
            return ApiResponse.error("Error while fetching tasks by status: " + ex.getMessage());
        }
    }


    public ApiResponse<Void> createTask(CreateTaskRequest createTaskRequest) {
        try {
            User assignee = userRepository.findById(createTaskRequest.getAssigneeId()).orElseThrow(() -> new EntityNotFoundException("Assignee not found"));
            User assigneer = userRepository.findById(createTaskRequest.getAssigneerId()).orElseThrow(() -> new EntityNotFoundException("Assigner not found"));

            if (!assignee.getRoles().stream().anyMatch(r -> r.getName() == ERole.SOFTWARE) || !assigneer.getRoles().stream().anyMatch(r -> r.getName() == ERole.SOFTWARE)) {
                return ApiResponse.error("Users is not Software User");
            }


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
                    .orElseThrow(() -> new EntityNotFoundException("Assignee Software User not found"));
            User assigner = userRepository.findById(request.getAssigneerId())
                    .orElseThrow(() -> new EntityNotFoundException("Assigner Software User  not found"));

            if (!assignee.getRoles().stream().anyMatch(r -> r.getName() == ERole.SOFTWARE) || !assigner.getRoles().stream().anyMatch(r -> r.getName() == ERole.SOFTWARE)) {
                return ApiResponse.error("Users is not Software User");
            }


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

    public ApiResponse<Page<TaskInfoResponse>> getTaskInfoListBySoftwareUserId(Long id, Pageable pageable) {
        try {
            SoftwareUser softwareUser = softwareUserRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Software User not found with ID: " + id));

            Page<Task> taskPage = taskRepository.findByAssigneeIdAndDeletedFalse(id, pageable);

            List<TaskInfoResponse> taskInfoResponseList = taskPage.getContent().stream()
                    .map(task -> {
                        SoftwareUser assigner = null;
                        if (task.getAssigner() != null) {
                            assigner = softwareUserRepository.findById(task.getAssigner().getId())
                                    .orElseThrow(() -> new EntityNotFoundException("Software User not found with ID: " + id));
                        }

                        return new TaskInfoResponse(
                                task.getTitle(),
                                task.getDescription(),
                                task.getStatus().name(),
                                task.getPriority().name(),
                                task.getDeadLine(),
                                task.getCreateTime(),
                                assigner != null ? assigner.getId() : null,  // Assigner's ID might be null
                                assigner != null ? assigner.getUser().getName() : null,  // Handle null assigner
                                assigner != null ? assigner.getUser().getSurname() : null,  // Handle null assigner
                                softwareUser.getId(),
                                softwareUser.getUser().getName(),
                                softwareUser.getUser().getSurname(),
                                convertToCommentResponse(task)
                        );
                    })
                    .collect(Collectors.toList());

            // PageResponse oluşturun
            Page<TaskInfoResponse> taskInfoPage = new PageImpl<>(taskInfoResponseList, pageable, taskPage.getTotalElements());

            return ApiResponse.success("Get Task By Software User ID Success", taskInfoPage);
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

            SoftwareUser assignee = softwareUserRepository.findById(task.getAssignee().getId()).get();
            SoftwareUser assigner = softwareUserRepository.findById(task.getAssigner().getId()).get();

            return ApiResponse.success("Get Task Info Success", new TaskInfoResponse(
                    task.getTitle(),
                    task.getDescription(),
                    task.getStatus().name(),
                    task.getPriority().name(),
                    task.getDeadLine(),
                    task.getCreateTime(),
                    assigner.getId(),
                    assigner.getUser().getName(),
                    assigner.getUser().getSurname(),
                    assignee.getId(),
                    assignee.getUser().getName(),
                    assignee.getUser().getSurname(),
                    convertToCommentResponse(task)
            ));
        } catch (EntityNotFoundException ex) {
            return ApiResponse.error("Task not found: " + ex.getMessage());
        } catch (Exception ex) {
            return ApiResponse.error("Error while fetching task info: " + ex.getMessage());
        }
    }
    }
