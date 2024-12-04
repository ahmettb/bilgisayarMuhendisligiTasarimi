package com.jobtrackingapp.software_service.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateTaskRequest {


    private String title;

    private String description;

    private String priority;

    private String status;

    private Long assigneeId;

    private Long assigneerId;

    private Date deadLine;

}
