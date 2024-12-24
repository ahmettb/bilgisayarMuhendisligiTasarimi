package com.jobtrackingapp.software_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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

    // Assignee ve Assigner ile SoftwareUser ilişkilendirildi
    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = false)
    private SoftwareUser assignee;

    @ManyToOne
    @JoinColumn(name = "assigner_id", nullable = false)
    private SoftwareUser assigner;

    private boolean deleted = false;
}
