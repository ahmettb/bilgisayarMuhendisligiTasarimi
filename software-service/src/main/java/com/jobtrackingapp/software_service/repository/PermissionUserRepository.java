package com.jobtrackingapp.software_service.repository;

import com.jobtrackingapp.software_service.model.PermissionUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionUserRepository extends JpaRepository<PermissionUser,Long> {
    Optional<List<PermissionUser>> findByUser_Id(Long userId);  // 'userId' ile sorgu yapar

}
