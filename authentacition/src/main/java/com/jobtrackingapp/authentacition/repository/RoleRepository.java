package com.jobtrackingapp.authentacition.repository;

import com.jobtrackingapp.authentacition.model.ERole;
import com.jobtrackingapp.authentacition.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role>findByName(ERole name);

}
