package com.jobtrackingapp.admin_service.service;

import com.jobtrackingapp.admin_service.model.response.ApiResponse;
import com.jobtrackingapp.admin_service.model.response.TaskInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SoftwareServiceClient {


    @Value("${software-service-url}")
    private String softwareServiceUrl;


    private final RestTemplate restTemplate;



    public ApiResponse<TaskInfoResponse> getTaskInfo(Long taskId) {
        String url = softwareServiceUrl + "/get/" + taskId;
        ResponseEntity<ApiResponse<TaskInfoResponse>> response =
                restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<ApiResponse<TaskInfoResponse>>() {});
        return response.getBody();
    }
}
