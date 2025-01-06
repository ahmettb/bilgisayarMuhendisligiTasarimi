package com.jobtrackingapp.admin_service.service;

import com.jobtrackingapp.admin_service.model.PermissionUser;
import com.jobtrackingapp.admin_service.model.User;
import com.jobtrackingapp.admin_service.model.request.CreatePermissionRequest;
import com.jobtrackingapp.admin_service.model.response.ApiResponse;
import com.jobtrackingapp.admin_service.model.response.PermissionResponse;
import com.jobtrackingapp.admin_service.model.response.UserResponse;
import com.jobtrackingapp.admin_service.repository.PermissionUserRepository;
import com.jobtrackingapp.admin_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class AdminService {

    private final PermissionUserRepository permissionUserRepository;
    private final UserRepository userRepository;

    public AdminService(PermissionUserRepository permissionUserRepository, UserRepository userRepository) {
        this.permissionUserRepository = permissionUserRepository;
        this.userRepository = userRepository;
    }

    public ApiResponse<Void> createPermission(CreatePermissionRequest request) {
        log.info("Creating permission request for user ID: {}", request.getUserId());
        try {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + request.getUserId()));
            log.debug("User found: {}", user);

            PermissionUser permissionUser = new PermissionUser();
            permissionUser.setUser(user);
            permissionUser.setDayCount(request.getDayCount());
            permissionUser.setReason(request.getReason());
            permissionUser.setStartDateOfPermission(request.getStartDateOfPermission());
            permissionUser.setAccepted(request.getAccepted());

            permissionUserRepository.save(permissionUser);
            log.info("Permission request created successfully for user ID: {}", request.getUserId());
            return ApiResponse.success("Permission Request Created Successfully", null);
        } catch (Exception e) {
            log.error("Error creating permission request for user ID: {}", request.getUserId(), e);
            return ApiResponse.error("Error creating permission request: " + e.getMessage());
        }
    }

    public ApiResponse<Void> approvePermission(Long permissionId) {
        log.info("Approving permission with ID: {}", permissionId);
        try {
            PermissionUser permission = permissionUserRepository.findById(permissionId)
                    .orElseThrow(() -> new EntityNotFoundException("Permission request not found with ID: " + permissionId));
            log.debug("Permission found: {}", permission);

            permission.setAccepted(true);
            permissionUserRepository.save(permission);
            log.info("Permission with ID: {} approved successfully", permissionId);
            return ApiResponse.success("Permission Approved Successfully", null);
        } catch (Exception e) {
            log.error("Error approving permission with ID: {}", permissionId, e);
            return ApiResponse.error("Error approving permission: " + e.getMessage());
        }
    }

    public ApiResponse<Void> rejectPermission(Long permissionId) {
        log.info("Rejecting permission with ID: {}", permissionId);
        try {
            PermissionUser permission = permissionUserRepository.findById(permissionId)
                    .orElseThrow(() -> new EntityNotFoundException("Permission request not found with ID: " + permissionId));
            log.debug("Permission found: {}", permission);

            permission.setAccepted(false);
            permissionUserRepository.save(permission);
            log.info("Permission with ID: {} rejected successfully", permissionId);
            return ApiResponse.success("Permission Rejected Successfully", null);
        } catch (Exception e) {
            log.error("Error rejecting permission with ID: {}", permissionId, e);
            return ApiResponse.error("Error rejecting permission: " + e.getMessage());
        }
    }

    public ApiResponse<List<PermissionResponse>> getPermissionsByUserId(Long userId) {
        log.info("Retrieving permissions for user ID: {}", userId);
        try {
            List<PermissionUser> permissions = permissionUserRepository.findAll()
                    .stream()
                    .filter(p -> p.getUser().getId()==userId)
                    .toList();

            if (permissions.isEmpty()) {
                log.warn("No permissions found for user ID: {}", userId);
                return ApiResponse.error("No permissions found for the given user");
            }

            List<PermissionResponse> permissionResponses = new ArrayList<>();
            for (PermissionUser permissionUser : permissions) {
                permissionResponses.add(permissionResponse(permissionUser));
            }
            log.info("Permissions retrieved successfully for user ID: {}", userId);
            return ApiResponse.success("Permissions Retrieved Successfully", permissionResponses);
        } catch (Exception e) {
            log.error("Error retrieving permissions for user ID: {}", userId, e);
            return ApiResponse.error("Error retrieving permissions for user: " + e.getMessage());
        }
    }

    public ApiResponse<List<PermissionResponse>> getPermissionsByStatus(Boolean accepted) {
        log.info("Retrieving permissions with status: {}", accepted);
        try {
            List<PermissionUser> permissions = permissionUserRepository.findAll()
                    .stream()
                    .filter(t -> t.getAccepted().equals(accepted))
                    .toList();

            if (permissions.isEmpty()) {
                log.warn("No permissions found with status: {}", accepted);
                return ApiResponse.error("No permissions found for the given status");
            }

            List<PermissionResponse> permissionResponses = new ArrayList<>();
            for (PermissionUser permissionUser : permissions) {
                permissionResponses.add(permissionResponse(permissionUser));
            }

            log.info("Permissions retrieved successfully with status: {}", accepted);
            return ApiResponse.success("Permissions Retrieved Successfully", permissionResponses);
        } catch (Exception e) {
            log.error("Error retrieving permissions with status: {}", accepted, e);
            return ApiResponse.error("Error retrieving permissions: " + e.getMessage());
        }
    }

    public ApiResponse<List<PermissionResponse>> getAllPermissions() {
        log.info("Retrieving all permissions");
        try {
            List<PermissionUser> permissions = permissionUserRepository.findAll();

            List<PermissionResponse> permissionResponses = new ArrayList<>();
            for (PermissionUser permissionUser : permissions) {
                permissionResponses.add(permissionResponse(permissionUser));
            }

            log.info("All permissions retrieved successfully");
            return ApiResponse.success("All Permissions Retrieved Successfully", permissionResponses);
        } catch (Exception e) {
            log.error("Error retrieving all permissions", e);
            return ApiResponse.error("Error retrieving all permissions: " + e.getMessage());
        }
    }

    private PermissionResponse permissionResponse(PermissionUser permissionUser) {
        log.debug("Mapping PermissionUser to PermissionResponse: {}", permissionUser);
        PermissionResponse response = new PermissionResponse();
        response.setPermissionId(permissionUser.getId());
        response.setAccepted(permissionUser.getAccepted());
        response.setDayCount(permissionUser.getDayCount());
        response.setStartDateOfPermission(permissionUser.getStartDateOfPermission());
        response.setName(permissionUser.getUser().getName());
        response.setSurname(permissionUser.getUser().getSurname());
        response.setRoleSet(permissionUser.getUser().getRoles());
        response.setReason(permissionUser.getReason());
        return response;
    }
}
