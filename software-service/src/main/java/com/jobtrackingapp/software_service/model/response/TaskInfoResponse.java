package com.jobtrackingapp.software_service.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TaskInfoResponse {


    private String title;
    private String description;
    private String status;
    private String priority;
    private Date deadline;
    private Date createdTime;
    private String assigneerName;
    private String assigneerSurname;

}
