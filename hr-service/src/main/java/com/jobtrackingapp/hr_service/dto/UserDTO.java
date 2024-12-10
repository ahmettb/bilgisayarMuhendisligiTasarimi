package com.jobtrackingapp.hr_service.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String role;  // Burada Role'ü string olarak tutabilirsiniz
    private String birthDate;
}
