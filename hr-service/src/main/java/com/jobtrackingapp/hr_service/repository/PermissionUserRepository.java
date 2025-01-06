package com.jobtrackingapp.hr_service.repository;


import com.jobtrackingapp.hr_service.entity.PermissionUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionUserRepository extends JpaRepository<PermissionUser,Long> {
   // Optional<List<PermissionUser>> findByUser_Id(Long userId);  // 'userId' ile sorgu yapar
    Page<PermissionUser> findByUser_Id(Long userId, Pageable pageable);

}
