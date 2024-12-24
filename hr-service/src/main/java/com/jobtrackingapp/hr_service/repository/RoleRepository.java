package com.jobtrackingapp.hr_service.repository;

import com.jobtrackingapp.hr_service.entity.Role;
import com.jobtrackingapp.hr_service.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>
{
   // Optional<Role> findByERole(ERole roleType);
}
