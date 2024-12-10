package com.jobtrackingapp.hr_service.entity.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponse {
    private String name;

    private String surname;

    private String email;

    private String password;

    private LocalDate birthDate;
}
