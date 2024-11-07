package com.cashregister.authentacition.model.request;

import com.cashregister.authentacition.model.ERole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {


    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String address;

    @NotBlank
    @Email
    private String email;


    private Set<ERole> roles;
}
