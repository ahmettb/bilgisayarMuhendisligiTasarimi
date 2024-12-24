package com.jobtrackingapp.marketing_service.entity.response;

import lombok.Data;

import java.util.Date;

@Data
public class PermissionResponse {

    private Long permissionId;

    private String userName;

    private String  surname;
    private Long userId;

    private Integer dayCount;

    private Boolean accepted=false;


    private Date startDateOfPermission;
}
