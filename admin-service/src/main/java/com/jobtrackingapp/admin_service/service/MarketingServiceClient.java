package com.jobtrackingapp.admin_service.service;

import com.jobtrackingapp.admin_service.model.response.CampaignResponse;
import com.jobtrackingapp.admin_service.model.request.CreateCampaignRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketingServiceClient {

    @Value("${marketing-service-url}")
    private String marketingServiceUrl;


    private final RestTemplate restTemplate;


    public CampaignResponse getCampaignResponse(Long campaignId) {
        String url = marketingServiceUrl + "/get/" + campaignId;
        ResponseEntity<CampaignResponse> response =
                restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<CampaignResponse>() {});
        return response.getBody();
    }


    public void   addCampaign(CreateCampaignRequest request) {
        String url = marketingServiceUrl + "/save" ;

                restTemplate.postForObject(url, request, String.class);

    }


    public List<CampaignResponse> getCampaignResponseList() {
        String url = marketingServiceUrl + "/get-all";
        ResponseEntity<List<CampaignResponse>> response =
                restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<CampaignResponse>>() {});
        return response.getBody();
    }

}
