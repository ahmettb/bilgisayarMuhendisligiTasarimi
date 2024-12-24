package com.jobtrackingapp.marketing_service.repository;

import com.jobtrackingapp.marketing_service.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICampaignRepository extends JpaRepository<Campaign,Long> {



    Optional<Campaign> findByIdAndDeletedFalse(Long id);
}
