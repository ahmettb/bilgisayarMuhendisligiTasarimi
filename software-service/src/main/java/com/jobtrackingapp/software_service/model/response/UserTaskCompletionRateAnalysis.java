package com.jobtrackingapp.software_service.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserTaskCompletionRateAnalysis {
    private double completionRate; // percentage

}
