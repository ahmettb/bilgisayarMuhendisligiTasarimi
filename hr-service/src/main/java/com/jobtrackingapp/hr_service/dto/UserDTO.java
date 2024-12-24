package com.jobtrackingapp.hr_service.dto;

import com.jobtrackingapp.hr_service.entity.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private Set<Role> role;
    private String address;
}
