package com.jobtrackingapp.marketing_service.entity.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCampaignStatusRequest {
    private  long id;

    private String status;

}
