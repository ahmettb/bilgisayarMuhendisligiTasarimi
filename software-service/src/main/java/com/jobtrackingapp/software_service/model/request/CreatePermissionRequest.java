package com.jobtrackingapp.software_service.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class CreatePermissionRequest {

    private Long userId;  // Kullanıcı ID'si, User nesnesi yerine ID gönderilir
    private Integer dayCount;
    private Boolean accepted = false;
    private Date startDateOfPermission;
}
