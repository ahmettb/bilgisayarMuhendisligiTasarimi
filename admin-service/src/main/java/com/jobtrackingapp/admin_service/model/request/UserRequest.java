package com.jobtrackingapp.admin_service.model.request;

import com.jobtrackingapp.admin_service.enums.ERole;
import lombok.Getter;
import lombok.Setter;

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
