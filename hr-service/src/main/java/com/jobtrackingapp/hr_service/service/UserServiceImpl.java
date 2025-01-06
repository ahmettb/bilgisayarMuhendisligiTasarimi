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
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final PermissionUserRepository permissionUserRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Override
    public ApiResponse<Void> addUser(UserRequest userDTO) {
        log.info("addUser method started with request: {}", userDTO);
        String authServiceUrl = "http://localhost:8500/api/auth/register";
        restTemplate.postForObject(authServiceUrl, userDTO, Void.class);
        log.info("addUser method successfully completed for user: {}", userDTO.getEmail());
        return ApiResponse.success("User added successfully", null);
    }


    @Override
    public ApiResponse<UserDTO> updateUser(Long id, UserRequest userRequest) {
        log.info("updateUser method started for user ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new EntityNotFoundException("User not found with ID " + id);
                });
        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setAddress(userRequest.getAddress());
        userRepository.save(user);
        log.info("updateUser method successfully completed for user ID: {}", id);
        return ApiResponse.success("User updated successfully", toDTO(user));
    }

    @Override
    public ApiResponse<Void> deleteUser(Long id) {
        log.info("deleteUser method started for user ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new EntityNotFoundException("User not found with ID " + id);
                });
        user.setDeleted(true);
        userRepository.save(user);

        log.info("deleteUser method successfully completed for user ID: {}", id);
        return ApiResponse.success("User deleted successfully", null);
    }

    @Override
    public ApiResponse<UserResponse> getUserById(Long id) {
        log.info("getUserById method started for user ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new EntityNotFoundException("User not found with ID " + id);
                });
        log.info("getUserById method completed successfully for user ID: {}", toResponse(user));
        return ApiResponse.success("User found", toResponse(user));
    }

    @Override
    public ApiResponse<User> getUserEntity(Long id) {
        log.info("getUserEntity method started for user ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new EntityNotFoundException("User not found with ID " + id);
                });
        log.info("getUserEntity method completed successfully for user ID: {}", id);
        return ApiResponse.success("User entity found", user);
    }

    @Override
    public ApiResponse<List<UserDTO>> getUsersByRole(ERole roleType) {
        log.info("getUsersByRole method started for role: {}", roleType);
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOList = users.stream()
                .filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals(roleType)))
                .map(this::toDTO)
                .collect(Collectors.toList());

        log.info("getUsersByRole method completed successfully for role: {}", roleType);
        return ApiResponse.success("Users retrieved by role", userDTOList);
    }

    private UserDTO toDTO(User user) {
        log.debug("toDTO method started for user: {}", user.getEmail());
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setRole(user.getRoles());
        log.debug("toDTO method completed for user: {}", user.getEmail());
        return userDTO;
    }

    private UserResponse toResponse(User user) {
        log.debug("toResponse method started for user: {}", user.getEmail());
        UserResponse userResponse = new UserResponse();
        userResponse.setName(user.getName());
        userResponse.setSurname(user.getSurname());
        userResponse.setEmail(user.getEmail());
        userResponse.setUsername(user.getUsername());
        userResponse.setRoleSet(user.getRoles());
        userResponse.setAddress(user.getAddress());
        log.debug("toResponse method completed for user: {}", user.getEmail());
        return userResponse;
    }

}