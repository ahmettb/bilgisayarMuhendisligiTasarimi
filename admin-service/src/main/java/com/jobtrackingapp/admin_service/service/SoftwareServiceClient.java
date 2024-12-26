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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
@RequiredArgsConstructor
public class SoftwareServiceClient {

    @Value("${software-service-url}")
    private String softwareServiceUrl;

    private final RestTemplate restTemplate;

    public ApiResponse<TaskInfoResponse> getTaskInfo(Long taskId) {
        String url = softwareServiceUrl + "/get/" + taskId;

        try {
            ResponseEntity<ApiResponse<TaskInfoResponse>> response =
                    restTemplate.exchange(url, HttpMethod.GET, null,
                            new ParameterizedTypeReference<ApiResponse<TaskInfoResponse>>() {});

            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Capture client/server-side HTTP error (4xx or 5xx) and return an error response
            return ApiResponse.error("Error occurred while fetching task info: " + e.getMessage());
        } catch (Exception e) {
            // Handle any other exceptions (e.g. network failure, timeouts)
            return ApiResponse.error("Unexpected error occurred: " + e.getMessage());
        }
    }
}