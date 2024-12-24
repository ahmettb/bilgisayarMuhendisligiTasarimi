package com.jobtrackingapp.admin_service.model.response;

import com.jobtrackingapp.admin_service.model.Role;
import lombok.Data;

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
