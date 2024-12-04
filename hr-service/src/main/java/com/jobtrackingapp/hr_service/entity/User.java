package com.jobtrackingapp.hr_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @ManyToOne
    private Role role;

    private LocalDate birthDate;

    private boolean isDeleted = false; // Soft delete için
}
