package com.jobtrackingapp.admin_service.service;

import com.jobtrackingapp.admin_service.model.request.UserRequest;
import com.jobtrackingapp.admin_service.model.response.UserResponse;
import com.jobtrackingapp.admin_service.model.response.ApiResponse;
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
public class HumanResourceClient {

    @Value("${hr-service-url}")
    private String hrServiceUrl;

    private final RestTemplate restTemplate;

    public ApiResponse<Void> addUser(UserRequest userRequest) {
        String url = "http://localhost:8500/api/auth/register";
        try {
            restTemplate.postForObject(url, userRequest, Void.class);
            return ApiResponse.success("User created successfully", null);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ApiResponse.error("Error occurred while creating user: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Unexpected error occurred: " + e.getMessage());
        }
    }

    public ApiResponse<UserResponse> getUserById(Long id) {
        String url = hrServiceUrl + "/get/" + id;
        try {
            ResponseEntity<UserResponse> response =
                    restTemplate.exchange(url, HttpMethod.GET, null,
                            new ParameterizedTypeReference<UserResponse>() {});
            return ApiResponse.success("User retrieved successfully", response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ApiResponse.error("Error occurred while fetching user: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Unexpected error occurred: " + e.getMessage());
        }
    }
}