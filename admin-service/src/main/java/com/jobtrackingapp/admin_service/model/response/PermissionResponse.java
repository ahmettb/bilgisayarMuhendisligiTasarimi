package com.jobtrackingapp.admin_service.model.response;

import com.jobtrackingapp.admin_service.model.Role;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class PermissionResponse {

    private Long permissionId;
    private String name;
    private String reason;
    private String surname;
    private Set<Role> roleSet;

    private Integer dayCount;

    private Boolean accepted=false;


    private Date startDateOfPermission;
}
