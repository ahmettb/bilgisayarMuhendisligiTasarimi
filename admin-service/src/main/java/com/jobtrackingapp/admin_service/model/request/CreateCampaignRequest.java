package com.jobtrackingapp.admin_service.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateCampaignRequest {


    private String name;

    private String description;

    private Date startDate;

    private Date endDate;

    private Double budget;

    private Long createdUserId;

    private int targetReach;
}
