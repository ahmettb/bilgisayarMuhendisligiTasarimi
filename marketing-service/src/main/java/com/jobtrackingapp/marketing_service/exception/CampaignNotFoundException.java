package com.jobtrackingapp.marketing_service.exception;

public class CampaignNotFoundException extends RuntimeException{

    public CampaignNotFoundException(String message){
        super(message);
    }
}
