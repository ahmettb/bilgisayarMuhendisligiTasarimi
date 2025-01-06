package com.jobtrackingapp.admin_service.service;

import com.jobtrackingapp.admin_service.model.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.List;

@Service
public class SoftwareServiceClient {

    @Value("${software-service-url}")
    private String softwareServiceUrl;

    private final RestTemplate restTemplate;

    public SoftwareServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ApiResponse<TaskInfoResponse> getTaskInfo(Long taskId,String authorization) {
        String url = softwareServiceUrl + "/get/" + taskId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<ApiResponse<TaskInfoResponse>> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity,
                            new ParameterizedTypeReference<ApiResponse<TaskInfoResponse>>() {});

            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ApiResponse.error("Error occurred while fetching task info: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Unexpected error occurred: " + e.getMessage());
        }
    }

    public ApiResponse<UserTaskCompletionRateAnalysis> getUserTaskCompletionRateAnalysis(Long userId, String authorization) {
        String url = softwareServiceUrl + "/get-task-completion-rate/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ApiResponse<UserTaskCompletionRateAnalysis>> response =
                restTemplate.exchange(url, HttpMethod.GET, entity,
                        new ParameterizedTypeReference<ApiResponse<UserTaskCompletionRateAnalysis>>() {});
        return response.getBody();
    }

    public ApiResponse<TaskStatusChangeAnalysis> getTaskStatusChangeAnalysis(Long userId, Date startDate, Date endDate, String authorization) {
        String url = softwareServiceUrl + "/get-tasks-by-user-and-date"; // No need to append userId directly in the URL

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", userId.toString());
        params.add("startDate", startDate.toString());
        params.add("endDate", endDate.toString());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<ApiResponse<TaskStatusChangeAnalysis>> response =
                restTemplate.exchange(url, HttpMethod.GET, entity,
                        new ParameterizedTypeReference<ApiResponse<TaskStatusChangeAnalysis>>() {});

        return response.getBody();
    }


    public ApiResponse<TaskStatusAnalysis> getTaskStatusAnalysis(Long userId, String authorization) {
        String url = softwareServiceUrl + "/get-task-status-analysis/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ApiResponse<TaskStatusAnalysis>> response =
                restTemplate.exchange(url, HttpMethod.GET, entity,
                        new ParameterizedTypeReference<ApiResponse<TaskStatusAnalysis>>() {});

        return response.getBody();
    }

    public ApiResponse<List<TaskInfoResponse>> getTasksByStatus(Long userId, String status, String authorization) {
        String url = softwareServiceUrl + "/get-task-by-status";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("userId", userId)
                .queryParam("status", status);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ApiResponse<List<TaskInfoResponse>>> response =
                restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
                        new ParameterizedTypeReference<ApiResponse<List<TaskInfoResponse>>>() {});

        return response.getBody();
    }

}