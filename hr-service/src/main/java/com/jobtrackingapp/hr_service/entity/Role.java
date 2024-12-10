package com.jobtrackingapp.hr_service.entity;

import com.jobtrackingapp.hr_service.enums.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
<<<<<<< HEAD
@Data
@Table(name = "rol")

=======
@Getter
@Setter
>>>>>>> 053b6fe4d27bc1e9d0a982a3bf7200b7cd500092
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType role;
}
