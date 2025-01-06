package com.jobtrackingapp.software_service.model.request;

import lombok.Data;

@Data
public class CommentRequest {


    private  String content;

    private Long userId;
    private Long taskId;

}
