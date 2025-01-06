package com.jobtrackingapp.software_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Entity
@Table
@Data
public class Comment extends BaseEntity {


    private String content;


    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    private Date createdAt=new Date();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
