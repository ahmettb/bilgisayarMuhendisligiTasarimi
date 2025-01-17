package com.jobtrackingapp.hr_service.entity.request;

import com.jobtrackingapp.hr_service.enums.ERole;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class UserRequest {

    private String name;

    private String username;

    private String address;
    private String surname;

    private String email;

    private String password;

    private Set<ERole> roles;
}
