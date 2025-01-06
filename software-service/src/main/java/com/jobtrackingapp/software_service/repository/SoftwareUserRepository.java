package com.jobtrackingapp.software_service.repository;

import com.jobtrackingapp.software_service.model.SoftwareUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SoftwareUserRepository extends JpaRepository<SoftwareUser,Long> {

    @Query("SELECT su FROM SoftwareUser su WHERE su.user.id = :userId")
    Optional<SoftwareUser> findSoftwareUserByUserId(@Param("userId") Long userId);}


