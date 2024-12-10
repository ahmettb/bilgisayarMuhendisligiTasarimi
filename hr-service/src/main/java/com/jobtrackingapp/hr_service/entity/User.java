package com.jobtrackingapp.hr_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Table(name = "users")
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
<<<<<<< HEAD
    private Role roles;
=======
    private Role role;  // Burada Role, RoleType enum yerine Role sınıfı olabilir.
>>>>>>> 053b6fe4d27bc1e9d0a982a3bf7200b7cd500092

    private LocalDate birthDate;

    private boolean isDeleted = false; // Soft delete için
}
