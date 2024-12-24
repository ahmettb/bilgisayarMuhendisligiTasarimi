package com.jobtrackingapp.hr_service.service;

import com.jobtrackingapp.hr_service.dto.UserDTO;
import com.jobtrackingapp.hr_service.entity.User;
import com.jobtrackingapp.hr_service.entity.request.UserRequest;
import com.jobtrackingapp.hr_service.entity.response.UserResponse;
import com.jobtrackingapp.hr_service.enums.ERole;
import com.jobtrackingapp.hr_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;



    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserDTO addUser(UserDTO userDTO) {
        return null;
    }
    public void addUser(UserRequest request) {

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        userRepository.save(user);

    }

    @Override
    public User getUserEntity(Long id) {
      return   userRepository.findById(id).orElseThrow(()->new EntityNotFoundException());

    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        return  null;
    }

    @Override
    public void deleteUser(Long id) {
        return  ;

    }

//    @Override
//    public UserDTO getUserById(Long id) {
//        return null;
//
//    }
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        UserResponse userResponse = new UserResponse();
        userResponse.setName(user.getName());
        userResponse.setSurname(user.getSurname());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        return userResponse;
    }

    @Override
    public List<UserDTO> getUsersByRole(ERole roletype) {

        return  null;

    }

    private UserDTO toDTO(User user) {
        return  null;



    }
}
