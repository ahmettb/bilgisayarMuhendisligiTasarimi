package com.jobtrackingapp.admin_service.service;

import com.jobtrackingapp.admin_service.model.response.CampaignResponse;
import com.jobtrackingapp.admin_service.model.request.CreateCampaignRequest;
import com.jobtrackingapp.admin_service.model.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
@Service
@RequiredArgsConstructor
@Log4j2
public class MarketingServiceClient {

    @Value("${marketing-service-url}")
    private String marketingServiceUrl;

    private final RestTemplate restTemplate;

    public ApiResponse<CampaignResponse> getCampaignResponse(@RequestParam("campaignId") Long campaignId, String authorization) {
        String url = marketingServiceUrl + "/get/";
        log.info("Fetching campaign details for campaignId: {}", campaignId);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorization);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<ApiResponse<CampaignResponse>> response =
                    restTemplate.exchange(url + campaignId, HttpMethod.GET, entity,
                            new ParameterizedTypeReference<ApiResponse<CampaignResponse>>() {});

            log.info("Successfully fetched campaign details for campaignId: {}", campaignId);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Client/Server error while fetching campaign details for campaignId: {}. Error: {}", campaignId, e.getMessage());
            return ApiResponse.error("Error occurred while fetching campaign: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while fetching campaign details for campaignId: {}. Error: {}", campaignId, e.getMessage());
            return ApiResponse.error("Unexpected error occurred: " + e.getMessage());
        }
    }

    public ApiResponse<Void> addCampaign(CreateCampaignRequest request, String authorization) {
        String url = marketingServiceUrl + "/save";
        log.info("Adding new campaign: {}", request);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorization);
            HttpEntity<CreateCampaignRequest> entity = new HttpEntity<>(request, headers);

            restTemplate.postForObject(url, entity, Void.class);
            log.info("Successfully created campaign: {}", request);
            return ApiResponse.success("Campaign created successfully", null);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Client/Server error while creating campaign. Request: {}. Error: {}", request, e.getMessage());
            return ApiResponse.error("Error occurred while creating campaign: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while creating campaign. Request: {}. Error: {}", request, e.getMessage());
            return ApiResponse.error("Unexpected error occurred: " + e.getMessage());
        }
    }

    public ApiResponse<List<CampaignResponse>> getCampaignResponseList(String authorization) {
        String url = marketingServiceUrl + "/get-all";
        log.info("Fetching all campaigns");

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorization);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<ApiResponse<List<CampaignResponse>>> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity,
                            new ParameterizedTypeReference<ApiResponse<List<CampaignResponse>>>() {});

            log.info("Successfully fetched all campaigns");
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Client/Server error while fetching all campaigns. Error: {}", e.getMessage());
            return ApiResponse.error("Error occurred while fetching campaign list: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while fetching all campaigns. Error: {}", e.getMessage());
            return ApiResponse.error("Unexpected error occurred: " + e.getMessage());
        }
    }
}
