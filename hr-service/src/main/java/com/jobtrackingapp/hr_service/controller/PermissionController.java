package com.jobtrackingapp.hr_service.controller;


import com.jobtrackingapp.hr_service.entity.request.CreatePermissionRequest;
import com.jobtrackingapp.hr_service.entity.response.ApiResponse;
import com.jobtrackingapp.hr_service.entity.response.PermissionResponse;
import com.jobtrackingapp.hr_service.service.PermissionService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("api/hr/permission")
@Log4j2
public class PermissionController {


    private final PermissionService permissionService;


    @GetMapping("get-permission/{id}")
    public ResponseEntity<ApiResponse<Page<PermissionResponse>>> getPermission(
            @PathVariable("id") Long id, Pageable pageable) {

        ApiResponse<Page<PermissionResponse>> response = permissionService.getPermission(id, pageable);

        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("accept-permission/{id}")
    public ApiResponse<Void> acceptPermission(@PathVariable Long id) {
        permissionService.acceptPermission(id);
        return ApiResponse.success("Permission accepted", null);
    }

    @PutMapping("reject-permission/{id}")
    public ApiResponse<Void> rejectPermission(@PathVariable Long id) {
        permissionService.rejectPermission(id);
        return ApiResponse.success("Permission accepted", null);
    }


    @PostMapping("add-permission")
    public ResponseEntity<ApiResponse<String>> createPermission(@RequestBody CreatePermissionRequest request) {
        log.info("Creating permission for user with ID: {}", request.getUserId());
        ApiResponse<String> response = permissionService.createPermission(request);
        if (response.isSuccess()) {
            log.info("Permission successfully created for user with ID: {}", request.getUserId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.error("Failed to create permission for user with ID: {}", request.getUserId());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("delete-permission/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") Long id) {
        log.info("Delete permission for with ID: {}", id);
        ApiResponse<Void> response = permissionService.deletePermission(id);
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.error("Failed to deleted permission for  with ID: {}", id);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
