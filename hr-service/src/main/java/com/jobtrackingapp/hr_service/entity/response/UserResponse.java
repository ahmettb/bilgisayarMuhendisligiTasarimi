package com.jobtrackingapp.hr_service.entity.response;

import com.jobtrackingapp.hr_service.entity.Role;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserResponse {
    private String name;

    private String surname;

    private String email;

    private String username;

    private String address;

    private Set<Role> roleSet;

}
