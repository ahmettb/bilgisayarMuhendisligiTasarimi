package com.jobtrackingapp.marketing_service.entity.request;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateCampaignRequest {


    private String name;

    private String description;

    private Date startDate;

    private Date endDate;

    private Double budget;

    private Double usedBudget;

    private String status;

    private int targetReach;
}
