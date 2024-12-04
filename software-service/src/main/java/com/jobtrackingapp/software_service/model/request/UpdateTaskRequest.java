package com.jobtrackingapp.software_service.model.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateTaskRequest {

    private Long taskId;

    private String status;

    private Date updateStatusDate;

}
