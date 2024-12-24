package com.jobtrackingapp.admin_service.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class CampaignResponse {

    private String userName;

    private  String userSurname;

    private Long userId;

    private String name;

    private String description;

    private Date startDate;

    private Date endDate;

    private Double budget;

    private Double usedBudget;

    private String status;

    private int targetReach;
}
