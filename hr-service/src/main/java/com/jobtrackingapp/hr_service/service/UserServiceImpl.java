package com.jobtrackingapp.hr_service.service;

import com.jobtrackingapp.hr_service.dto.UserDTO;
import com.jobtrackingapp.hr_service.entity.User;
import com.jobtrackingapp.hr_service.entity.request.UserRequest;
import com.jobtrackingapp.hr_service.entity.response.UserResponse;
import com.jobtrackingapp.hr_service.enums.RoleType;
import com.jobtrackingapp.hr_service.repository.UserRepository;
import com.jobtrackingapp.software_service.model.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

<<<<<<< HEAD
=======

>>>>>>> 053b6fe4d27bc1e9d0a982a3bf7200b7cd500092
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
<<<<<<< HEAD
    public UserDTO addUser(UserDTO userDTO) {
return  null;
=======
    public void addUser(UserRequest request) {

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setBirthDate(request.getBirthDate());
        userRepository.save(user);

>>>>>>> 053b6fe4d27bc1e9d0a982a3bf7200b7cd500092
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
<<<<<<< HEAD
        return  null;

=======
        return null;
>>>>>>> 053b6fe4d27bc1e9d0a982a3bf7200b7cd500092
    }

    @Override
    public void deleteUser(Long id) {
        return  ;

    }

    @Override
<<<<<<< HEAD
    public UserDTO getUserById(Long id) {
        return  null;

=======
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        UserResponse userResponse = new UserResponse();
        userResponse.setName(user.getName());
        userResponse.setSurname(user.getSurname());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setBirthDate(user.getBirthDate());
        return userResponse;
>>>>>>> 053b6fe4d27bc1e9d0a982a3bf7200b7cd500092
    }

    @Override
    public List<UserDTO> getUsersByRole(RoleType roletype) {
<<<<<<< HEAD
        return  null;

    }

    private UserDTO toDTO(User user) {
        return  null;

=======
        return null;
>>>>>>> 053b6fe4d27bc1e9d0a982a3bf7200b7cd500092
    }
}
