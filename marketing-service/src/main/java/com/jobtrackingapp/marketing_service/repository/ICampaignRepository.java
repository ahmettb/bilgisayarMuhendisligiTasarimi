package com.jobtrackingapp.marketing_service.repository;

import com.jobtrackingapp.marketing_service.entity.Campaign;
import com.jobtrackingapp.marketing_service.entity.constant.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ICampaignRepository extends JpaRepository<Campaign,Long> {


    @Query("SELECT c FROM Campaign c WHERE c.endDate < :endDate AND c.status NOT IN :excludedStatuses")
    List<Campaign> findAllByEndDateBeforeAndStatusNotIn(Date endDate, List<CampaignStatus> excludedStatuses);

    Optional<Campaign> findByIdAndDeletedFalse(Long id);
}
