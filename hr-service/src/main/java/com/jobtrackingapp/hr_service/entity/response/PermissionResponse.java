package com.jobtrackingapp.hr_service.entity.response;

import lombok.Data;

import java.util.Date;

@Data
public class PermissionResponse {

    private Long permissionId;
    UserResponse userResponse;

    private Integer dayCount;

    private Boolean accepted=false;


    private Date startDateOfPermission;
}
