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


    @GetMapping("/software/task/{id}")
    public ResponseEntity<ApiResponse<TaskInfoResponse>> getSoftwareTask(@PathVariable("id") Long id) {
        return ResponseEntity.ok(softwareServiceClient.getTaskInfo(id));
    }

    @GetMapping("/marketing/get-all-campaign/")
    public ResponseEntity<List<CampaignResponse>> getCampaignResponseList() {
        return ResponseEntity.ok(marketingServiceClient.getCampaignResponseList());
    }


    @GetMapping("/marketing/get-campaign/{id}")
    public ResponseEntity<CampaignResponse> getCampaignResponse(@PathVariable("id") Long id) {
        return ResponseEntity.ok(marketingServiceClient.getCampaignResponse(id));
    }


    @PostMapping("/marketing/save-campaign/")
    public ResponseEntity<?> addCampaign(@RequestBody CreateCampaignRequest request) {
        marketingServiceClient.addCampaign(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/hr/add-user")
    public ResponseEntity<?> addUser(@RequestBody UserRequest request)  {
        humanResourceClient.addUser(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/hr/get-user")
    public ResponseEntity<?> getUserById(@PathVariable("id")Long id)  {
        humanResourceClient.getUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }




}
