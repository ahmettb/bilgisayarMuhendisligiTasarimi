package com.jobtrackingapp.authentacition.controller;


import com.jobtrackingapp.authentacition.model.UserInfo;
import com.jobtrackingapp.authentacition.model.request.LoginRequest;
import com.jobtrackingapp.authentacition.model.request.SignupRequest;
import com.jobtrackingapp.authentacition.model.response.AuthentacitionResponse;
import com.jobtrackingapp.authentacition.repository.RoleRepository;
import com.jobtrackingapp.authentacition.repository.UserRepository;
import com.jobtrackingapp.authentacition.service.AuthService;

import jwt.JwtUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@Log4j2
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<AuthentacitionResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {
        log.info("AuthController: login request received for username: {}", loginRequest.getUsername());
        ResponseEntity<AuthentacitionResponse> response = ResponseEntity.ok(authService.login(loginRequest));
        log.info("AuthController: login response generated for username: {}", loginRequest.getUsername());
        return response;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SignupRequest signUpRequest) throws Exception {
        log.info("AuthController: register request received for email: {}", signUpRequest.getEmail());
        authService.registerUser(signUpRequest);
        log.info("AuthController: user registered successfully for username: {}", signUpRequest.getUsername());
        return ResponseEntity.ok("Register Success");
    }

    @GetMapping("/validate/{token}")
    public ResponseEntity<String> validate(@PathVariable("token") String token) throws Exception {
        log.info("AuthController: validate request received for token.");
        boolean isValidate = authService.validate(token);
        log.info("AuthController: token validation result: {}", isValidate ? "Valid" : "Invalid");
        return ResponseEntity.ok(isValidate ? "Is valid" : "Not valid token");
    }

    @GetMapping("get-user-info")
    public ResponseEntity<UserInfo> getUserInfo(@RequestHeader("Authorization") String token) throws Exception {
        log.info("AuthController: getUserInfo request received.");
        UserInfo userInfo = authService.getUserInfo(token);
        log.info("AuthController: user info retrieved successfully for token.");
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @GetMapping("get-current-user")
    public ResponseEntity<UserInfo> getCurrentInfo() {
        log.info("AuthController: getCurrentInfo request received.");
        UserInfo userInfo = authService.getCurrentUser();
        log.info("AuthController: current user info retrieved successfully.");
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }
}
