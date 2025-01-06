package com.jobtrackingapp.admin_service.model.response;

import com.jobtrackingapp.admin_service.model.Role;
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
