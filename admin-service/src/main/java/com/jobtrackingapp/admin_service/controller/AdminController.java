package com.jobtrackingapp.admin_service.controller;

import com.jobtrackingapp.admin_service.enums.ERole;
import com.jobtrackingapp.admin_service.model.PermissionUser;
import com.jobtrackingapp.admin_service.model.User;
import com.jobtrackingapp.admin_service.model.request.CreateCampaignRequest;
import com.jobtrackingapp.admin_service.model.request.UserRequest;
import com.jobtrackingapp.admin_service.model.request.CreatePermissionRequest;
import com.jobtrackingapp.admin_service.model.response.*;
import com.jobtrackingapp.admin_service.service.HumanResourceClient;
import com.jobtrackingapp.admin_service.service.MarketingServiceClient;
import com.jobtrackingapp.admin_service.service.SoftwareServiceClient;
import com.jobtrackingapp.admin_service.service.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/admin")
@AllArgsConstructor
@Log4j2
public class AdminController {

    private final SoftwareServiceClient softwareServiceClient;
    private final HumanResourceClient humanResourceClient;
    private final AdminService adminService;
    private final MarketingServiceClient marketingServiceClient;


    @GetMapping("/task/{id}")
    public ResponseEntity<ApiResponse<TaskInfoResponse>> getSoftwareTask(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String authorization) {
        log.info("Fetching software task info for taskId: {}", id);
        ApiResponse<TaskInfoResponse> response = softwareServiceClient.getTaskInfo(id, authorization);
        if (response.isSuccess()) {
            log.info("Successfully fetched software task info for taskId: {}", id);
            return ResponseEntity.ok(response);
        } else {
            log.error("Failed to fetch software task info for taskId: {}. Error: {}", id, response.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    @GetMapping("/task/completion-rate")
    public ResponseEntity<ApiResponse<UserTaskCompletionRateAnalysis>> getUserTaskCompletionRateAnalysis(
            @RequestParam("userId") Long userId,
            @RequestHeader("Authorization") String authorization) {
        log.info("Fetching task completion rate for userId: {}", userId);
        ApiResponse<UserTaskCompletionRateAnalysis> response = softwareServiceClient.getUserTaskCompletionRateAnalysis(userId, authorization);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/task/status-change")
    public ResponseEntity<ApiResponse<TaskStatusChangeAnalysis>> getTaskStatusChangeAnalysis(
            @RequestParam("userId") Long userId,
            @RequestParam("startDate") Date startDate,
            @RequestParam("endDate") Date endDate,
            @RequestHeader("Authorization") String authorization) {
        log.info("Fetching task status change analysis for userId: {}", userId);
        ApiResponse<TaskStatusChangeAnalysis> response = softwareServiceClient.getTaskStatusChangeAnalysis(userId, startDate, endDate, authorization);
        return ResponseEntity.ok(response);
    }

        @GetMapping("/task/status-analysis")
    public ResponseEntity<ApiResponse<TaskStatusAnalysis>> getTaskStatusAnalysis(
            @RequestParam("userId") Long userId,
            @RequestHeader("Authorization") String authorization) {
        log.info("Fetching task status analysis for userId: {}", userId);
        ApiResponse<TaskStatusAnalysis> response = softwareServiceClient.getTaskStatusAnalysis(userId, authorization);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/task/get-by-status")
    public ResponseEntity<ApiResponse<List<TaskInfoResponse>>> getTasksByStatus(
            @RequestParam("userId") Long userId,
            @RequestParam("status") String status,
            @RequestHeader("Authorization") String authorization) {
        log.info("Fetching tasks by status for userId: {} and status: {}", userId, status);
        ApiResponse<List<TaskInfoResponse>> response = softwareServiceClient.getTasksByStatus(userId, status, authorization);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-user")
    public ResponseEntity<ApiResponse<Void>> addUser(
            @RequestBody UserRequest request,
            @RequestHeader("Authorization") String authorization) {
        log.info("Adding new user: {}", request);
        ApiResponse<Void> response = humanResourceClient.addUser(request, authorization);
        if (response.isSuccess()) {
            log.info("Successfully added new user: {}", request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            log.error("Failed to add new user: {}. Error: {}", request, response.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable("id") Long id,
            @RequestBody UserRequest request,
            @RequestHeader("Authorization") String authorization) {
        log.info("Updating user with ID: {}", id);
        ApiResponse<UserDTO> response = humanResourceClient.updateUser(id, request, authorization);
        if (response.isSuccess()) {
            log.info("Successfully updated user with ID: {}", id);
            return ResponseEntity.ok(response);
        } else {
            log.error("Failed to update user with ID: {}. Error: {}", id, response.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String authorization) {
        log.info("Deleting user with ID: {}", id);
        ApiResponse<Void> response = humanResourceClient.deleteUser(id, authorization);
        if (response.isSuccess()) {
            log.info("Successfully deleted user with ID: {}", id);
            return ResponseEntity.ok(response);
        } else {
            log.error("Failed to delete user with ID: {}. Error: {}", id, response.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/get-user/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String authorization) {
        log.info("Fetching user details for ID: {}", id);
        ApiResponse<UserResponse> response = humanResourceClient.getUserById(id, authorization);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            log.error("Failed to fetch user details for ID: {}. Error: {}", id, response.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/get-user-entity/{id}")
    public ResponseEntity<ApiResponse<User>> getUserEntityById(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String authorization) {
        log.info("Fetching user entity for ID: {}", id);
        ApiResponse<User> response = humanResourceClient.getUserEntityById(id, authorization);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            log.error("Failed to fetch user entity for ID: {}. Error: {}", id, response.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/users-by-role")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getUsersByRole(
            @RequestParam("role") ERole role,
            @RequestHeader("Authorization") String authorization) {
        log.info("Fetching users by role: {}", role);
        ApiResponse<List<UserDTO>> response = humanResourceClient.getUsersByRole(role, authorization);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            log.error("Failed to fetch users by role: {}. Error: {}", role, response.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




    @PostMapping("/save/campaign")
    public ResponseEntity<ApiResponse<Void>> createCampaign(
            @RequestBody CreateCampaignRequest request,
            @RequestHeader("Authorization") String authorization) {
        log.info("Creating new campaign: {}", request);
        ApiResponse<Void> response = marketingServiceClient.addCampaign(request, authorization);
        if (response.isSuccess()) {
            log.info("Successfully created campaign: {}", request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            log.error("Failed to create campaign: {}. Error: {}", request, response.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/campaign/{campaignId}")
    public ResponseEntity<ApiResponse<CampaignResponse>> getCampaignById(
            @PathVariable("campaignId") Long campaignId,
            @RequestHeader("Authorization") String authorization) {
        log.info("Fetching campaign details for campaignId: {}", campaignId);
        ApiResponse<CampaignResponse> response = marketingServiceClient.getCampaignResponse(campaignId, authorization);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            log.error("Failed to fetch campaign details for campaignId: {}. Error: {}", campaignId, response.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/get-all/campaigns")
    public ResponseEntity<ApiResponse<List<CampaignResponse>>> getAllCampaigns(
            @RequestHeader("Authorization") String authorization) {
        log.info("Fetching all campaigns");
        ApiResponse<List<CampaignResponse>> response = marketingServiceClient.getCampaignResponseList(authorization);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            log.error("Failed to fetch all campaigns. Error: {}", response.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/add/permission")
    public ResponseEntity<ApiResponse<Void>> createPermissionRequest(
            @RequestBody CreatePermissionRequest request,
            @RequestHeader("Authorization") String authorization) {
        log.info("Creating permission request: {}", request);
        ApiResponse<Void> response = adminService.createPermission(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/permission/approve/{permissionId}")
    public ResponseEntity<ApiResponse<Void>> approvePermission(
            @PathVariable("permissionId") Long permissionId,
            @RequestHeader("Authorization") String authorization) {
        log.info("Approving permission for permissionId: {}", permissionId);
        ApiResponse<Void> response = adminService.approvePermission(permissionId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/permission/reject/{permissionId}")
    public ResponseEntity<ApiResponse<Void>> rejectPermission(
            @PathVariable("permissionId") Long permissionId,
            @RequestHeader("Authorization") String authorization) {
        log.info("Rejecting permission for permissionId: {}", permissionId);
        ApiResponse<Void> response = adminService.rejectPermission(permissionId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/permissions/user/{userId}")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getPermissionsByUserId(
            @PathVariable("userId") Long userId,
            @RequestHeader("Authorization") String authorization) {
        log.info("Fetching permissions for userId: {}", userId);
        ApiResponse<List<PermissionResponse>> response = adminService.getPermissionsByUserId(userId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/permissions/status/{accepted}")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getPermissionsByStatus(
            @PathVariable("accepted") Boolean accepted,
            @RequestHeader("Authorization") String authorization) {
        log.info("Fetching permissions with status: {}", accepted);
        ApiResponse<List<PermissionResponse>> response = adminService.getPermissionsByStatus(accepted);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




    @GetMapping("/get-all/permissions")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAllPermissions() {
        log.info("Fetching all permission requests.");
        ApiResponse<List<PermissionResponse>> response = adminService.getAllPermissions();
        if (response.isSuccess()) {
            log.info("Successfully fetched all permission requests.");
            return ResponseEntity.ok(response);
        } else {
            log.error("Failed to fetch all permission requests. Error: {}", response.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
