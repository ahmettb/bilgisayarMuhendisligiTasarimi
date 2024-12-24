package com.jobtrackingapp.software_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "software_user")
public class SoftwareUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // Task sınıfıyla ilişkiyi doğru kurmak için OneToMany ilişkisini ekledik
    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasksAssigned = new ArrayList<>();

    @OneToMany(mappedBy = "assigner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasksAssignedByMe = new ArrayList<>();
}
