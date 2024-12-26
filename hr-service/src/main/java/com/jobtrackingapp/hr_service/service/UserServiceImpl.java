package com.jobtrackingapp.hr_service.service;

import com.jobtrackingapp.hr_service.dto.UserDTO;
import com.jobtrackingapp.hr_service.entity.PermissionUser;
import com.jobtrackingapp.hr_service.entity.User;
import com.jobtrackingapp.hr_service.entity.request.CreatePermissionRequest;
import com.jobtrackingapp.hr_service.entity.request.UserRequest;
import com.jobtrackingapp.hr_service.entity.response.PermissionResponse;
import com.jobtrackingapp.hr_service.entity.response.UserResponse;
import com.jobtrackingapp.hr_service.entity.response.ApiResponse;
import com.jobtrackingapp.hr_service.enums.ERole;
import com.jobtrackingapp.hr_service.repository.PermissionUserRepository;
import com.jobtrackingapp.hr_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final PermissionUserRepository permissionUserRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Override
    public ApiResponse<Void> addUser(UserRequest userDTO) {
        String authServiceUrl = "http://localhost:8500/api/auth/register";
        restTemplate.postForObject(authServiceUrl, userDTO, Void.class);
        return ApiResponse.success("User added successfully", null);
    }

    @Override
    public ApiResponse<Void> createPermission(CreatePermissionRequest request) {
        PermissionUser permissionUser = new PermissionUser();
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User Not Found With ID " + request.getUserId()));

        permissionUser.setDayCount(request.getDayCount());
        permissionUser.setStartDateOfPermission(request.getStartDateOfPermission());
        permissionUser.setAccepted(false);
        permissionUser.setUser(user);
        permissionUserRepository.save(permissionUser);

        return ApiResponse.success("Permission created successfully", null);
    }

    @Override
    public ApiResponse<List<PermissionResponse>> getPermissionByUserId(Long id) {
        List<PermissionUser> permissionUser = permissionUserRepository.findByUser_Id(id)
                .orElseThrow(() -> new EntityNotFoundException("Permissions not found for user with ID " + id));

        List<PermissionResponse> responseList = permissionUser.stream().map(permission -> {
            UserResponse userResponse = toResponse(permission.getUser());
            PermissionResponse permissionResponse = new PermissionResponse();
            permissionResponse.setUserResponse(userResponse);
            permissionResponse.setStartDateOfPermission(permission.getStartDateOfPermission());
            permissionResponse.setDayCount(permission.getDayCount());
            permissionResponse.setAccepted(permission.getAccepted());
            permissionResponse.setPermissionId(permission.getId());
            return permissionResponse;
        }).collect(Collectors.toList());

        return ApiResponse.success("Permissions retrieved successfully", responseList);
    }

    @Override
    public ApiResponse<Void> acceptPermission(Long id) {
        PermissionUser permissionUser = permissionUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found with ID " + id));

        permissionUser.setAccepted(true);
        permissionUserRepository.save(permissionUser);

        return ApiResponse.success("Permission accepted", null);
    }

    @Override
    public ApiResponse<UserDTO> updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID " + id));
        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setAddress(user.getAddress());
        userRepository.save(user);
        return ApiResponse.success("User updated successfully", toDTO(user));
    }

    @Override
    public ApiResponse<Void> deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID " + id));
        user.setDeleted(true);
        userRepository.save(user);
        return ApiResponse.success("User deleted successfully", null);
    }

    @Override
    public ApiResponse<UserResponse> getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID " + id));
        return ApiResponse.success("User found", toResponse(user));
    }

    @Override
    public ApiResponse<User> getUserEntity(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID " + id));
        return ApiResponse.success("User entity found", user);
    }

    @Override
    public ApiResponse<List<UserDTO>> getUsersByRole(ERole roleType) {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOList = users.stream()
                .filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals(roleType)))
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ApiResponse.success("Users retrieved by role", userDTOList);
    }

    private UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setRole(user.getRoles());
        return userDTO;
    }

    private UserResponse toResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setName(user.getName());
        userResponse.setSurname(user.getSurname());
        userResponse.setEmail(user.getEmail());
        userResponse.setUsername(user.getUsername());
        userResponse.setRoleSet(user.getRoles());
        userResponse.setAddress(user.getAddress());
        return userResponse;
    }
}