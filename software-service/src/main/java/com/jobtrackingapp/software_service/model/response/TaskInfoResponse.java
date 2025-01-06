package com.jobtrackingapp.software_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TaskInfoResponse {


    private String title;
    private String description;
    private String status;
    private String priority;
    private Date deadline;
    private Date createdTime;

    private Long assigneerId;
    private String assigneerName;
    private String assigneerSurname;


    private Long assigneeId;
    private String assigneeName;
    private String assigneeSurname;


    List<CommentResponse> commentResponseList=new ArrayList<>();
}
