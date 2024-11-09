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

        log.info("AuthController :login method entered with request {}", loginRequest.getUsername());
        return ResponseEntity.ok(authService.login(loginRequest));

    }



    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignupRequest signUpRequest) throws Exception {

        log.info("AuthController : register method entered with request {}", signUpRequest.toString());

        authService.registerUser(signUpRequest);
        return ResponseEntity.ok("Register success");
    }

    @GetMapping("/validate/{token}")
    public ResponseEntity<String> validate(@PathVariable("token")String token) throws Exception {
        log.info("AuthController : validate method entered ");

    boolean isValidate=authService.validate(token);
        log.info("AuthController : validate method completed successfuly ");

        return ResponseEntity.ok(isValidate ?"Is valid":"Not valid token");
    }

    @GetMapping("get-user-info")
    public ResponseEntity<UserInfo>getUserInfo(@RequestHeader("Authorization")String token) throws Exception
    {

       return new ResponseEntity<>( authService.getUserInfo(token),HttpStatus.OK);


    }

    @GetMapping("get-current-user")
    public ResponseEntity<UserInfo>getCurrentInfo()
    {

        return  new ResponseEntity<>(authService.getCurrentUser(),HttpStatus.OK);

    }


}