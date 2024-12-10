package com.jobtrackingapp.software_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table
public class Task extends BaseEntity {


    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    private Date createTime;

    private Date deadLine;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigneer_id")
    private User assigner;


    private boolean deleted=false;

}
