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
        PermissionUser permissionUser = new PermissionUser();

        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("User Not Found With ID" + request.getUserId()));

        permissionUser.setDayCount(request.getDayCount());
        permissionUser.setStartDateOfPermission(request.getStartDateOfPermission());
        permissionUser.setAccepted(false);
        permissionUser.setUser(user);
        permissionUserRepository.save(permissionUser);

        return ApiResponse.success("Permission Request Success", null);

    }

    public ApiResponse<List<PermissionResponse>> getPermission(Long id)
    {
        List<PermissionUser> permissionUser= permissionUserRepository.findByUser_Id(id).orElseThrow(()->new EntityNotFoundException());

        List<PermissionResponse> responseList=new ArrayList<>();
        for(PermissionUser permissionUser1:permissionUser)
        {
            PermissionResponse permissionResponse=new PermissionResponse();
            permissionResponse.setSurname(permissionUser1.getUser().getSurname());
            permissionResponse.setUserName(permissionUser1.getUser().getName());
            permissionResponse.setUserId(permissionUser1.getUser().getId());
            permissionResponse.setStartDateOfPermission(permissionUser1.getStartDateOfPermission());
            permissionResponse.setDayCount(permissionUser1.getDayCount());
            permissionResponse.setAccepted(permissionUser1.getAccepted());
            permissionResponse.setPermissionId(permissionUser1.getId());

            responseList.add(permissionResponse);
        }


        return  ApiResponse.success("Get Permission Success",responseList);

    }


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

            // Assigner SoftwareUser'ı bul veya yeni oluştur
            SoftwareUser assignerSoftware = softwareUserRepository.findSoftwareUserByUserId(createTaskRequest.getAssigneerId())
                    .orElseGet(() -> {
                        SoftwareUser newAssignerSoftware = new SoftwareUser();
                        newAssignerSoftware.setUser(assigneer); // Assigneer kullanıcıyı ata
                        return softwareUserRepository.save(newAssignerSoftware); // Yeni SoftwareUser'ı kaydet
                    });

            // Assignee SoftwareUser'ı bul veya yeni oluştur
            SoftwareUser assigneSoftware = softwareUserRepository.findSoftwareUserByUserId(createTaskRequest.getAssigneeId())
                    .orElseGet(() -> {
                        SoftwareUser newAssigneSoftware = new SoftwareUser();
                        newAssigneSoftware.setUser(assignee); // Assignee kullanıcıyı ata
                        // Yeni bir task listesi oluşturup görevi ekle
                        List<Task> taskList = new ArrayList<>();
                        taskList.add(task); // Task'ı listeye ekle
                      //  newAssigneSoftware.setTasksAssigned(taskList); // SoftwareUser'a görevleri ata

                        return softwareUserRepository.save(newAssigneSoftware); // Yeni SoftwareUser'ı kaydet
                    });

            // Task'ı SoftwareUser'lara ata
           // assignerSoftware.getTasksAssignedByMe().add(task);
           // assigneSoftware.getTasksAssigned().add(task);
            task.setAssignee(assigneSoftware);
            task.setAssigner(assignerSoftware);

            // Task'ı kaydet
            taskRepository.save(task);

            return ApiResponse.success("Task Created", null);
        } catch (Exception exception) {
            return ApiResponse.error(exception.getMessage());
        }
    }


    public ApiResponse<Void> updateTaskStatus(UpdateTaskRequest updateTaskRequest) {

        try {
            Task task = taskRepository.findByIdAndDeletedFalse(updateTaskRequest.getTaskId()).orElseThrow(() -> new EntityNotFoundException());
            TaskStatus taskStatus = TaskStatus.valueOf(updateTaskRequest.getStatus());
            task.setStatus(taskStatus);
            taskRepository.save(task);
            return ApiResponse.success("Task Status Updated", null);

        } catch (Exception exception) {
            return ApiResponse.error(exception.getMessage());

        }

    }

    public ApiResponse<Void> deleteTask(Long taskId) {
        Task task = taskRepository.findByIdAndDeletedFalse(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));
        task.setDeleted(true);
        taskRepository.save(task);
        return ApiResponse.success("Task deleted successfully", null);
    }

    public ApiResponse<Void> updateTask(Long taskId, CreateTaskRequest request) {
        Task task = taskRepository.findByIdAndDeletedFalse(taskId)
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
        // task.setAssignee(assignee);
        // task.setAssigner(assigner);

        taskRepository.save(task);
        return ApiResponse.success("Task updated successfully", null);
    }
    public ApiResponse<List<TaskInfoResponse>>getTaskInfoListBuSoftwareUserId(Long id)
    {

      SoftwareUser softwareUser = softwareUserRepository.findSoftwareUserByUserId(id).orElseThrow(()->new EntityNotFoundException("Software User Not Found with ID"+id));


        List<TaskInfoResponse>taskInfoResponseList=new ArrayList<>();

        for(Task task : softwareUser.getTasksAssigned().stream().filter(task->task.isDeleted()==false).toList())
        {
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

            ));

        }
        return ApiResponse.success("Get Task By Software User ID Success",taskInfoResponseList);


    }

    public ApiResponse<TaskInfoResponse> getTaskInfo(Long taskId) {

        Task task = taskRepository.findByIdAndDeletedFalse(taskId)
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
