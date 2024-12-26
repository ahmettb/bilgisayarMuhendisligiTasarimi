package com.jobtrackingapp.admin_service.service;

import com.jobtrackingapp.admin_service.model.response.CampaignResponse;
import com.jobtrackingapp.admin_service.model.request.CreateCampaignRequest;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketingServiceClient {

    @Value("${marketing-service-url}")
    private String marketingServiceUrl;

    private final RestTemplate restTemplate;

    public ApiResponse<CampaignResponse> getCampaignResponse(Long campaignId) {
        String url = marketingServiceUrl + "/get/" + campaignId;
        try {
            ResponseEntity<CampaignResponse> response =
                    restTemplate.exchange(url, HttpMethod.GET, null,
                            new ParameterizedTypeReference<CampaignResponse>() {});
            return ApiResponse.success("Campaign retrieved successfully", response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ApiResponse.error("Error occurred while fetching campaign: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Unexpected error occurred: " + e.getMessage());
        }
    }

    public ApiResponse<Void> addCampaign(CreateCampaignRequest request) {
        String url = marketingServiceUrl + "/save";
        try {
            restTemplate.postForObject(url, request, String.class);
            return ApiResponse.success("Campaign created successfully", null);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ApiResponse.error("Error occurred while creating campaign: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Unexpected error occurred: " + e.getMessage());
        }
    }

    public ApiResponse<List<CampaignResponse>> getCampaignResponseList() {
        String url = marketingServiceUrl + "/get-all";
        try {
            ResponseEntity<List<CampaignResponse>> response =
                    restTemplate.exchange(url, HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<CampaignResponse>>() {});
            return ApiResponse.success("Campaign list retrieved successfully", response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ApiResponse.error("Error occurred while fetching campaign list: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Unexpected error occurred: " + e.getMessage());
        }
    }
}