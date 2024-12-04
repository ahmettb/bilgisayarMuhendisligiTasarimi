package com.jobtrackingapp.software_service.repository;

import com.jobtrackingapp.software_service.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
}
