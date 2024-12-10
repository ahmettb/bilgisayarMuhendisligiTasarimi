package com.jobtrackingapp.hr_service.service;

import com.jobtrackingapp.hr_service.dto.UserDTO;
import com.jobtrackingapp.hr_service.entity.User;
import com.jobtrackingapp.hr_service.enums.RoleType;
import com.jobtrackingapp.hr_service.repository.UserRepository;
import com.jobtrackingapp.software_service.model.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) {
return  null;
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        return  null;

    }

    @Override
    public void deleteUser(Long id) {
        return  ;

    }

    @Override
    public UserDTO getUserById(Long id) {
        return  null;

    }

    @Override
    public List<UserDTO> getUsersByRole(RoleType roletype) {
        return  null;

    }

    private UserDTO toDTO(User user) {
        return  null;

    }
}
