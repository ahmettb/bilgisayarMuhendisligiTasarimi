package com.jobtrackingapp.marketing_service.repository;

import com.jobtrackingapp.marketing_service.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICampaignRepository extends JpaRepository<Campaign,Long> {



    Optional<Campaign> findByIdAndDeletedFalse(Long id);
}
