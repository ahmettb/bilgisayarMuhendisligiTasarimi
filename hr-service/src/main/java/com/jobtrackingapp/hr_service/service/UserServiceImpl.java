package com.jobtrackingapp.hr_service.service;

import com.jobtrackingapp.hr_service.dto.UserDTO;
import com.jobtrackingapp.hr_service.entity.PermissionUser;
import com.jobtrackingapp.hr_service.entity.User;
import com.jobtrackingapp.hr_service.entity.request.CreatePermissionRequest;
import com.jobtrackingapp.hr_service.entity.request.UserRequest;
import com.jobtrackingapp.hr_service.entity.response.PermissionResponse;
import com.jobtrackingapp.hr_service.entity.response.UserResponse;
import com.jobtrackingapp.hr_service.enums.ERole;
import com.jobtrackingapp.hr_service.repository.PermissionUserRepository;
import com.jobtrackingapp.hr_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private  final PermissionUserRepository permissionUserRepository;

    private final UserRepository userRepository;

    private final RestTemplate restTemplate;

    @Override
    public void addUser(UserRequest userDTO) {
        String authServiceUrl = "http://localhost:8500/api/auth/register";
        restTemplate.postForObject(authServiceUrl, userDTO, Void.class);
    }

    @Override
    public void createPermission(CreatePermissionRequest request)
    {
        PermissionUser permissionUser=new PermissionUser();

        User user=userRepository.findById(request.getUserId()).orElseThrow(()->new EntityNotFoundException("User Not Found With ID"+request.getUserId()));

        permissionUser.setDayCount(request.getDayCount());
        permissionUser.setStartDateOfPermission(request.getStartDateOfPermission());
        permissionUser.setAccepted(false);
        permissionUser.setUser(user);
        permissionUserRepository.save(permissionUser);

    }

@Override
    public List< PermissionResponse> getPermissionByUserId(Long id)
    {
        List< PermissionUser> permissionUser= permissionUserRepository.findByUser_Id(id).orElseThrow(()->new EntityNotFoundException());
        List< PermissionResponse> responseList=new ArrayList<>();

        for(PermissionUser permissionUser1: permissionUser)
        {

            UserResponse userResponse= toResponse(permissionUser1.getUser());
            PermissionResponse permissionResponse=new PermissionResponse();
            permissionResponse.setUserResponse(userResponse);
            permissionResponse.setStartDateOfPermission(permissionUser1.getStartDateOfPermission());
            permissionResponse.setDayCount(permissionUser1.getDayCount());
            permissionResponse.setAccepted(permissionUser1.getAccepted());
            permissionResponse.setPermissionId(permissionUser1.getId());

            responseList.add(permissionResponse);

        }

        return  responseList;

    }

    @Override
    public void acceptPermission(Long id)
    {
        PermissionUser permissionUser=permissionUserRepository.findById(id).orElseThrow(()->new EntityNotFoundException());

        permissionUser.setAccepted(true);

        permissionUserRepository.save(permissionUser);

    }


    @Override
    public User getUserEntity(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    @Override
    public UserDTO updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setAddress(user.getAddress());
        userRepository.save(user);
        return toDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        user.setDeleted(true);
        userRepository.save(user);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        return toResponse(user);
    }

    @Override
    public List<UserDTO> getUsersByRole(ERole roleType) {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals(roleType)))
                .map(this::toDTO)
                .collect(Collectors.toList());
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
