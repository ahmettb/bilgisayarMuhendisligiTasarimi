package com.jobtrackingapp.hr_service.service;

import com.jobtrackingapp.hr_service.dto.UserDTO;
import com.jobtrackingapp.hr_service.entity.User;
import com.jobtrackingapp.hr_service.entity.request.CreatePermissionRequest;
import com.jobtrackingapp.hr_service.entity.request.UserRequest;
import com.jobtrackingapp.hr_service.entity.response.ApiResponse;
import com.jobtrackingapp.hr_service.entity.response.PermissionResponse;
import com.jobtrackingapp.hr_service.entity.response.UserResponse;
import com.jobtrackingapp.hr_service.enums.ERole;



import java.util.List;

public interface UserService {
    ApiResponse<Void> addUser(UserRequest request);
    ApiResponse<User>  getUserEntity(Long id);
    ApiResponse<UserDTO> updateUser(Long id,UserRequest userRequest);
    ApiResponse<Void> deleteUser(Long id);
    ApiResponse<UserResponse> getUserById(Long id);
    ApiResponse<List<UserDTO>> getUsersByRole(ERole roletype);
}