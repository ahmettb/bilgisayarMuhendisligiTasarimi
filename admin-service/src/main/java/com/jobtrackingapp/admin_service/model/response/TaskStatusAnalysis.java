package com.jobtrackingapp.admin_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskStatusAnalysis {

    private int toDo;
    private int inProgress;
    private int completed;
}
