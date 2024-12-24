package com.jobtrackingapp.marketing_service.controller;

import com.jobtrackingapp.marketing_service.entity.request.CreateCampaignRequest;
import com.jobtrackingapp.marketing_service.entity.request.CreatePermissionRequest;
import com.jobtrackingapp.marketing_service.entity.request.UpdateCampaignRequest;
import com.jobtrackingapp.marketing_service.entity.request.UpdateCampaignStatusRequest;
import com.jobtrackingapp.marketing_service.entity.response.CampaignResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jobtrackingapp.marketing_service.service.CampaignService;

import java.util.List;

@RestController
@RequestMapping("api/campaign/")
@AllArgsConstructor
public class CampaignController {


    private final CampaignService campaignService;



    @PostMapping("add-permission")
    public ResponseEntity<?> createPermission(@RequestBody CreatePermissionRequest request)
    {campaignService.createPermission(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("save")
    private ResponseEntity<?> addCampaign(@RequestBody CreateCampaignRequest request) {
        campaignService.saveCampaign(request);
        return ResponseEntity.ok("Campaign Saved Successfully");
    }

    @GetMapping("get/{id}")
    private ResponseEntity<CampaignResponse> getCampaignById(@PathVariable("id") long id) {
        return new ResponseEntity<>(campaignService.getCampaignById(id), HttpStatus.OK);
    }


    @GetMapping("get-all")
    private ResponseEntity<List<CampaignResponse>> getAllCampaign() {
        return new ResponseEntity<>(campaignService.getAllCampaign(), HttpStatus.OK);
    }


    @PutMapping("update")
    private ResponseEntity<?> updateCampaign(@RequestBody UpdateCampaignRequest request) {
        campaignService.updateCampaign(request);
        return ResponseEntity.ok("Campaign Updated Successfully");
    }

    @PutMapping("update-status")
    private ResponseEntity<?> updateCampaignStatus(@RequestBody UpdateCampaignStatusRequest request) {
        campaignService.updateCampaignStatus(request);
        return ResponseEntity.ok("Campaign Status Updated Successfully");
    }

    @DeleteMapping("delete/{id}")
    private ResponseEntity<?> deleteCampaign(@PathVariable("id") long id) {
        campaignService.deleteCampaignById(id);
        return ResponseEntity.ok("Campaign Deleted Successfully");
    }
}
