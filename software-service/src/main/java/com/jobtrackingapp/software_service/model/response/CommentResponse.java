package com.jobtrackingapp.software_service.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class CommentResponse {


    private String content;

    private String name;
    private String surname;

    private Date commentTime;


}
