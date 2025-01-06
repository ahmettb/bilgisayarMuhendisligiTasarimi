package com.jobtrackingapp.software_service.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class CreatePermissionRequest {

    private Long userId;
    private Integer dayCount;
    private Boolean accepted = false;
    private Date startDateOfPermission;
}
