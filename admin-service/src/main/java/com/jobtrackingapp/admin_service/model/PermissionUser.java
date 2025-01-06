package com.jobtrackingapp.admin_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "permission_user")
public class PermissionUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer dayCount;

    private String reason;

    private Boolean accepted=false;


    private Date startDateOfPermission;


}
