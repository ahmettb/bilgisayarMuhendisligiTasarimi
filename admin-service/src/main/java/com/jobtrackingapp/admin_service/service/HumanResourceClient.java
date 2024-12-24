package com.jobtrackingapp.admin_service.service;


import com.jobtrackingapp.admin_service.model.request.UserRequest;
import com.jobtrackingapp.admin_service.model.response.CampaignResponse;
import com.jobtrackingapp.admin_service.model.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class HumanResourceClient {
    @Value("${hr-service-url}")
    private String hrServiceUrl;
    private final RestTemplate restTemplate;


    public void addUser(UserRequest userRequest) {
        String url = "http://localhost:8500/api/auth/register";
                restTemplate.postForObject(url, userRequest, Void.class);
    }

    public ResponseEntity<UserResponse>  getUserById(Long id) {
        String url = hrServiceUrl + "/get/" + id;
        ResponseEntity<UserResponse> response =
                restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<UserResponse>() {
                        });

        return  response;
    }


}
