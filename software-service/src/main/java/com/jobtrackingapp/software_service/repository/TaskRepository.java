package com.jobtrackingapp.software_service.repository;

import com.jobtrackingapp.software_service.model.Task;
import com.jobtrackingapp.software_service.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long> {
    Optional<Task> findByIdAndDeletedFalse(Long id);

        // Assignee (Atanan kullanıcı) üzerinden sayfalama
        Page<Task> findByAssigneeIdAndDeletedFalse(Long assigneeId, Pageable pageable);


   List< Task> findByAssigneeIdAndDeletedFalse(Long assigneeId);

    // Assigner (Atayan kullanıcı) üzerinden sayfalama
    List<Task> findByAssignee_IdAndCreateTimeBetween(Long userId, Date startDate, Date endDate);


    Page<Task> findByAssignerIdAndDeletedFalse(Long assignerId, Pageable pageable);

    Page<Task> findByStatusAndDeletedFalse(TaskStatus status, Pageable pageable);

}
