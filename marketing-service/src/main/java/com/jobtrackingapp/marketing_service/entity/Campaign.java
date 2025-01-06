package com.jobtrackingapp.marketing_service.entity;

import com.jobtrackingapp.marketing_service.entity.constant.CampaignStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table
public class Campaign extends BaseEntity {



    private String name;

    private String description;

    private Date startDate;

    private Date endDate;

    private Double budget;

    private Double usedBudget;

    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

    private int targetReach;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
