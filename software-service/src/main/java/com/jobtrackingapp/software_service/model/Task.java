package com.jobtrackingapp.software_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "task")
public class Task extends BaseEntity {

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    private Date createTime;
    private Date deadLine;

    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = false)
    private SoftwareUser assignee;


    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "assigner_id", nullable = false)
    private SoftwareUser assigner;

    private boolean deleted = false;
}
