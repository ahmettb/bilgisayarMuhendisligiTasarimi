package com.jobtrackingapp.admin_service.repository;

import com.jobtrackingapp.admin_service.enums.ERole;
import com.jobtrackingapp.admin_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role>findByName(ERole name);

}
