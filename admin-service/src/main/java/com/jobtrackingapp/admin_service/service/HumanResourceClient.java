package com.jobtrackingapp.admin_service.service;

import com.jobtrackingapp.admin_service.enums.ERole;
import com.jobtrackingapp.admin_service.model.User;
import com.jobtrackingapp.admin_service.model.request.UserRequest;
import com.jobtrackingapp.admin_service.model.response.UserDTO;
import com.jobtrackingapp.admin_service.model.response.UserResponse;
import com.jobtrackingapp.admin_service.model.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;


import com.jobtrackingapp.admin_service.model.request.UserRequest;
import com.jobtrackingapp.admin_service.model.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;


@Service
@RequiredArgsConstructor
@Log4j2
public class HumanResourceClient {

    @Value("${hr-service-url}")
    private String hrServiceUrl;


    @Value("${auth-service-url}")
    private String authUrl;

    private final RestTemplate restTemplate;

    public ApiResponse<Void> addUser(UserRequest userRequest, String authorization) {
        log.info("Requesting to add user: {}", userRequest);
        String url = authUrl + "/register";

        try {

            restTemplate.postForObject(url, userRequest, String.class);
            log.info("User successfully added: {}", userRequest);
            return ApiResponse.success("User created successfully", null);
        } catch (Exception e) {
            log.error("Error occurred while creating user: {}", userRequest, e);
            return ApiResponse.error("Error occurred while creating user: " + e.getMessage());
        }
    } public ApiResponse<UserDTO> updateUser(Long id, UserRequest userRequest, String authorization) {
        log.info("Requesting to update user: {}", userRequest);
        String url = hrServiceUrl + "/update/" + id;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorization);
            HttpEntity<UserRequest> entity = new HttpEntity<>(userRequest, headers);

            ResponseEntity<ApiResponse<UserDTO>> response = restTemplate.exchange(url, HttpMethod.PUT, entity,new ParameterizedTypeReference<ApiResponse<UserDTO>>() {});
            return response.getBody();
        } catch (Exception e) {
            log.error("Error occurred while updating user: {}", userRequest, e);
            return ApiResponse.error("Error occurred while updating user: " + e.getMessage());
        }
    }
    public ApiResponse<Void> deleteUser(Long id, String authorization) {
        log.info("Requesting to delete user with id: {}", id);
        String url = hrServiceUrl + "/delete/" + id;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorization);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
            log.info("User deleted successfully with id: {}", id);
            return ApiResponse.success("User deleted successfully", null);
        } catch (Exception e) {
            log.error("Error occurred while deleting user with id: {}", id, e);
            return ApiResponse.error("Error occurred while deleting user: " + e.getMessage());
        }
    } public ApiResponse<UserResponse> getUserById(Long id, String authorization) {
        log.info("Requesting user details for id: {}", id);
        String url = hrServiceUrl + "/get/" + id;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorization);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<ApiResponse<UserResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<UserResponse>>() {}
            );

            return  response.getBody();
        } catch (Exception e) {
            log.error("Error occurred while fetching user details for id: {}", id, e);
            return ApiResponse.error("Error occurred while fetching user details: " + e.getMessage());
        }
    }

    public ApiResponse<User> getUserEntityById(Long id, String authorization) {
        log.info("Requesting user entity for id: {}", id);
        String url = hrServiceUrl + "/get-user-entity/" + id;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorization);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<ApiResponse<User>> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<ApiResponse<User>>() {});
            return response.getBody();
        } catch (Exception e) {
            log.error("Error occurred while fetching user entity for id: {}", id, e);
            return ApiResponse.error("Error occurred while fetching user entity: " + e.getMessage());
        }
    }
    public ApiResponse<List<UserDTO>> getUsersByRole(ERole role, String authorization) {
        log.info("Requesting users by role: {}", role);
        String url = hrServiceUrl + "/role/" + role;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorization);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<ApiResponse<List<UserDTO>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<List<UserDTO>>>() {}
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Error occurred while fetching users by role: {}", role, e);
            return ApiResponse.error("Error occurred while fetching users by role: " + e.getMessage());
        }
    }
}
