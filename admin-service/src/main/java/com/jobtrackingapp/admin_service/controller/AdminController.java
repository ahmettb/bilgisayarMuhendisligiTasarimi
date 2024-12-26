package com.jobtrackingapp.admin_service.controller;

import com.jobtrackingapp.admin_service.model.request.UserRequest;
import com.jobtrackingapp.admin_service.model.response.ApiResponse;
import com.jobtrackingapp.admin_service.model.response.CampaignResponse;
import com.jobtrackingapp.admin_service.model.response.TaskInfoResponse;
import com.jobtrackingapp.admin_service.model.request.CreateCampaignRequest;
import com.jobtrackingapp.admin_service.service.HumanResourceClient;
import com.jobtrackingapp.admin_service.service.MarketingServiceClient;
import com.jobtrackingapp.admin_service.service.SoftwareServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final SoftwareServiceClient softwareServiceClient;
    private final MarketingServiceClient marketingServiceClient;
    private final HumanResourceClient humanResourceClient;

    // Get software task info by ID
    @GetMapping("/software/task/{id}")
    public ResponseEntity<ApiResponse<TaskInfoResponse>> getSoftwareTask(@PathVariable("id") Long id) {
        ApiResponse<TaskInfoResponse> response = softwareServiceClient.getTaskInfo(id);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get all marketing campaigns
    @GetMapping("/marketing/get-all-campaign/")
    public ResponseEntity<ApiResponse<List<CampaignResponse>>> getCampaignResponseList() {
        ApiResponse<List<CampaignResponse>> response = marketingServiceClient.getCampaignResponseList();
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get specific campaign by ID
    @GetMapping("/marketing/get-campaign/{id}")
    public ResponseEntity<ApiResponse<CampaignResponse>> getCampaignResponse(@PathVariable("id") Long id) {
        ApiResponse<CampaignResponse> response = marketingServiceClient.getCampaignResponse(id);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Add a new marketing campaign
    @PostMapping("/marketing/save-campaign/")
    public ResponseEntity<ApiResponse<Void>> addCampaign(@RequestBody CreateCampaignRequest request) {
        ApiResponse<Void> response = marketingServiceClient.addCampaign(request);
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Add a new user to HR
    @PostMapping("/hr/add-user")
    public ResponseEntity<ApiResponse<Void>> addUser(@RequestBody UserRequest request) {
        ApiResponse<Void> response = humanResourceClient.addUser(request);
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get user info by ID
    @GetMapping("/hr/get-user/{id}")
    public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable("id") Long id) {
        ApiResponse<?> response = humanResourceClient.getUserById(id);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}