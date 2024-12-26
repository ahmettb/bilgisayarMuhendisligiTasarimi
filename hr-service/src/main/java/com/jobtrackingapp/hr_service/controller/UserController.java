package com.jobtrackingapp.hr_service.controller;

import com.jobtrackingapp.hr_service.dto.UserDTO;
import com.jobtrackingapp.hr_service.entity.User;
import com.jobtrackingapp.hr_service.entity.request.CreatePermissionRequest;
import com.jobtrackingapp.hr_service.entity.request.UserRequest;
import com.jobtrackingapp.hr_service.entity.response.ApiResponse;
import com.jobtrackingapp.hr_service.entity.response.PermissionResponse;
import com.jobtrackingapp.hr_service.entity.response.UserResponse;
import com.jobtrackingapp.hr_service.enums.ERole;
import com.jobtrackingapp.hr_service.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("save")
    public ApiResponse<Void> addUser(@RequestBody UserRequest userRequest) {
        return userService.addUser(userRequest);
    }

    @PutMapping("update/{id}")
    public ApiResponse<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        return userService.updateUser(id, request);
    }

    @PutMapping("accept-permission/{id}")
    public ApiResponse<Void> acceptPermission(@PathVariable Long id) {
        userService.acceptPermission(id);
        return ApiResponse.success("Permission accepted", null);
    }

    @GetMapping("get-permission/{id}")
    public ApiResponse<List<PermissionResponse>> getPermissionByUserId(@PathVariable Long id) {
        return userService.getPermissionByUserId(id);
    }

    @PostMapping("create-permission")
    public ApiResponse<Void> createPermission(@RequestBody CreatePermissionRequest request) {
        userService.createPermission(request);
        return ApiResponse.success("Permission created successfully", null);
    }

    @DeleteMapping("delete/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success("User deleted successfully", null);
    }

    @GetMapping("get/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("get-user-entity/{id}")
    public ApiResponse<User> getUserEntityById(@PathVariable("id") Long id) {
        return userService.getUserEntity(id);
    }

    @GetMapping("/role/{role}")
    public ApiResponse<List<UserDTO>> getUsersByRole(@PathVariable ERole role) {
        return userService.getUsersByRole(role);
    }
}