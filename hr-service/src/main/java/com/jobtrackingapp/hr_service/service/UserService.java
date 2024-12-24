package com.jobtrackingapp.hr_service.service;

import com.jobtrackingapp.hr_service.dto.UserDTO;
import com.jobtrackingapp.hr_service.entity.User;
import com.jobtrackingapp.hr_service.entity.request.UserRequest;
import com.jobtrackingapp.hr_service.entity.response.UserResponse;
import com.jobtrackingapp.hr_service.enums.ERole;



import java.util.List;

public interface UserService {
    void addUser(UserRequest request);
    User getUserEntity(Long id);

    UserDTO updateUser(Long id,UserDTO userDTO);
    void deleteUser(Long id);
    UserResponse getUserById(Long id);
    List<UserDTO> getUsersByRole(ERole roletype);

}
