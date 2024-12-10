package com.jobtrackingapp.hr_service.entity;

import com.jobtrackingapp.hr_service.enums.RoleType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "rol")

public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,unique = true)
    private RoleType roles;

}
