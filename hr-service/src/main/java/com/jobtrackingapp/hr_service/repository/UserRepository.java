package com.jobtrackingapp.hr_service.repository;

import com.jobtrackingapp.hr_service.entity.User;
import com.jobtrackingapp.hr_service.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>
{
    List<User> findAllByRole_Role(RoleType roleType);
}
