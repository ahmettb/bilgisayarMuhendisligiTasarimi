package com.jobtrackingapp.hr_service.controller;

import com.jobtrackingapp.hr_service.dto.UserDTO;
import com.jobtrackingapp.hr_service.entity.User;
import com.jobtrackingapp.hr_service.entity.request.CreatePermissionRequest;
import com.jobtrackingapp.hr_service.entity.request.UserRequest;
import com.jobtrackingapp.hr_service.entity.response.PermissionResponse;
import com.jobtrackingapp.hr_service.entity.response.UserResponse;
import com.jobtrackingapp.hr_service.enums.ERole;
import com.jobtrackingapp.hr_service.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping ("save")
    public void addUser(UserRequest userRequest)
    {
        userService.addUser(userRequest);
    }

    @PutMapping("update/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        return userService.updateUser(id, request);
    }

    @PutMapping("accept-permission/{id}")
    public void acceptPermission(@PathVariable Long id) {
        userService.acceptPermission(id);
    }

    @GetMapping("get-permission/{id}")
    public List<PermissionResponse> getPermissionByUserId(@PathVariable Long id) {
      return   userService.getPermissionByUserId(id);
    }

    @PostMapping("create-permission")
    public void  createPermisson(@RequestBody CreatePermissionRequest request) {
          userService.createPermission(request);
    }



    @DeleteMapping("delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("get/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("get-user-entity/{id}")
    public User getUserEntityById(@PathVariable("id")Long id)
    {
            return  userService.getUserEntity(id);
    }

    @GetMapping("/role/{role}")
    public List<UserDTO> getUsersByRole(@PathVariable ERole role) {
        return userService.getUsersByRole(role);
    }
}
