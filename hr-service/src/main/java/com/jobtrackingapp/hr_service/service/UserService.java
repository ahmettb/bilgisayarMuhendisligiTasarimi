package com.jobtrackingapp.hr_service.service;

import com.jobtrackingapp.hr_service.dto.UserDTO;
import com.jobtrackingapp.hr_service.enums.RoleType;


import java.util.List;

public interface UserService {
    UserDTO addUser(UserDTO userDTO);
    UserDTO updateUser(Long id,UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO getUserById(Long id);
    List<UserDTO> getUsersByRole(RoleType roletype);

}
