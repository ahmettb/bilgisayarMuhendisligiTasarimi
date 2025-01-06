package com.jobtrackingapp.admin_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskStatusChangeAnalysis {

    private int newTasks;
    private int inProgressTasks;
    private int completedTasks;

}
