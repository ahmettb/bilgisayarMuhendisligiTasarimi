package com.jobtrackingapp.marketing_service.repository;

import com.jobtrackingapp.marketing_service.entity.MarketingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MarketingUserRepository extends JpaRepository<MarketingUser,Long> {


    @Query("SELECT su FROM MarketingUser su WHERE su.user.id = :userId")
    Optional<MarketingUser> findMarketingUserByUserId(@Param("userId") Long userId);


}

