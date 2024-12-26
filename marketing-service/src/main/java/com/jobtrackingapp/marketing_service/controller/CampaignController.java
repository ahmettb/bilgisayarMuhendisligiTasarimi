package com.jobtrackingapp.marketing_service.controller;

import com.jobtrackingapp.marketing_service.entity.request.CreateCampaignRequest;
import com.jobtrackingapp.marketing_service.entity.request.CreatePermissionRequest;
import com.jobtrackingapp.marketing_service.entity.request.UpdateCampaignRequest;
import com.jobtrackingapp.marketing_service.entity.request.UpdateCampaignStatusRequest;
import com.jobtrackingapp.marketing_service.entity.response.CampaignResponse;
import com.jobtrackingapp.marketing_service.service.CampaignService;
import com.jobtrackingapp.marketing_service.entity.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/campaign/")
@AllArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @PostMapping("add-permission")
    public ResponseEntity<ApiResponse<Void>> createPermission(@RequestBody CreatePermissionRequest request) {
        ApiResponse<Void> response = campaignService.createPermission(request);
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("save")
    private ResponseEntity<ApiResponse<Void>> addCampaign(@RequestBody CreateCampaignRequest request) {
        ApiResponse<Void> response = campaignService.saveCampaign(request);
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.CREATED); // 201 Created status
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400 Bad Request for errors
        }
    }

    @GetMapping("get/{id}")
    private ResponseEntity<ApiResponse<CampaignResponse>> getCampaignById(@PathVariable("id") long id) {
        ApiResponse<CampaignResponse> response = campaignService.getCampaignById(id);
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    @GetMapping("get-all")
    private ResponseEntity<ApiResponse<List<CampaignResponse>>> getAllCampaign() {
        ApiResponse<List<CampaignResponse>> response = campaignService.getAllCampaign();
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400 Bad Request for any error
        }
    }

    @PutMapping("update")
    private ResponseEntity<ApiResponse<Void>> updateCampaign(@RequestBody UpdateCampaignRequest request) {
        ApiResponse<Void> response = campaignService.updateCampaign(request);
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("update-status")
    private ResponseEntity<ApiResponse<Void>> updateCampaignStatus(@RequestBody UpdateCampaignStatusRequest request) {
        ApiResponse<Void> response = campaignService.updateCampaignStatus(request);
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @DeleteMapping("delete/{id}")
    private ResponseEntity<ApiResponse<Void>> deleteCampaign(@PathVariable("id") long id) {
        ApiResponse<Void> response = campaignService.deleteCampaignById(id);
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }
}