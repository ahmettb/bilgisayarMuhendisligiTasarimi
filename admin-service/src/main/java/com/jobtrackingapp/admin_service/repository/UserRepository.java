package com.jobtrackingapp.admin_service.repository;

import com.jobtrackingapp.admin_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User >findByUsername(String username);
   boolean existsByEmail(String mail);

    Optional<User>findByUsernameAndDeletedFalse(String username);

}
