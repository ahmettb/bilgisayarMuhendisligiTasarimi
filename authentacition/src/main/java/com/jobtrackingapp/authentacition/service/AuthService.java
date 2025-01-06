package com.jobtrackingapp.authentacition.service;

import com.jobtrackingapp.authentacition.model.Role;
import com.jobtrackingapp.authentacition.model.User;
import com.jobtrackingapp.authentacition.model.UserInfo;
import com.jobtrackingapp.authentacition.model.request.LoginRequest;
import com.jobtrackingapp.authentacition.model.request.SignupRequest;
import com.jobtrackingapp.authentacition.model.response.AuthentacitionResponse;
import com.jobtrackingapp.authentacition.repository.RoleRepository;
import com.jobtrackingapp.authentacition.repository.UserRepository;
import com.jobtrackingapp.authentacition.exception.*;
import jwt.JwtUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
@Service
@Log4j2
public class AuthService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    public boolean validate(String token) {
        log.info("AuthService: validating token.");
        boolean isValid = jwtUtils.validateJwtToken(token);
        log.info("AuthService: token validation result: {}", isValid ? "Valid" : "Invalid");
        return isValid;
    }

    public void registerUser(SignupRequest signupRequest) throws Exception {
        log.info("AuthService: registering user with email: {}", signupRequest.getEmail());
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            log.error("AuthService: email already exists: {}", signupRequest.getEmail());
            throw new EmailAlreadyExist("Mail already exists");
        }

        userRepository.findByUsernameAndDeletedFalse(signupRequest.getUsername()).ifPresent(user -> {
            log.error("AuthService: username already exists: {}", signupRequest.getUsername());
            throw new UsernameAlreadyExist("Username already exists");
        });

        User user = new User();
        user.setName(signupRequest.getName());
        user.setSurname(signupRequest.getSurname());
        user.setEmail(signupRequest.getEmail());
        user.setAddress(signupRequest.getAddress());
        user.setUsername(signupRequest.getUsername());
        user.setPassword(encoder.encode(signupRequest.getPassword()));

        Set<Role> roleSet = new HashSet<>();
        signupRequest.getRoles().forEach(rol -> {
            Role role = roleRepository.findByName(rol).orElseThrow(() -> {
                log.error("AuthService: role not found: {}", rol);
                return new RoleNotFound("Role not found");
            });
            roleSet.add(role);
        });
        user.setRoles(roleSet);

        userRepository.save(user);
        log.info("AuthService: user registered successfully with username: {}", signupRequest.getUsername());
    }

    public AuthentacitionResponse login(LoginRequest loginRequest) throws Exception {
        log.info("AuthService: logging in user: {}", loginRequest.getUsername());
        User user = userRepository.findByUsernameAndDeletedFalse(loginRequest.getUsername()).orElseThrow(() -> {
            log.error("AuthService: user not found: {}", loginRequest.getUsername());
            return new UserNotFound("User not found");
        });

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtils.generateJwtToken(authentication, user.getRoles());
            log.info("AuthService: token generated successfully for user: {}", loginRequest.getUsername());

            AuthentacitionResponse authentacitionResponse = new AuthentacitionResponse();
            authentacitionResponse.setUsername(loginRequest.getUsername());
            authentacitionResponse.setAccesToken(token);

            log.info("AuthService: user logged in successfully: {}", loginRequest.getUsername());
            return authentacitionResponse;

        } catch (Exception e) {
            log.error("AuthService: login failed for username: {}", loginRequest.getUsername(), e);
            throw new LoginException("Username or password is wrong");
        }
    }

    public UserInfo getUserInfo(String token) {
        log.info("AuthService: retrieving user info from token.");
        String username = jwtUtils.getUserNameFromJwtToken(token);
        User user = userRepository.findByUsernameAndDeletedFalse(username).orElseThrow(() -> {
            log.error("AuthService: user not found for token.");
            return new UserNotFound("User not found");
        });

        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setName(user.getName());
        userInfo.setSurname(user.getSurname());
        userInfo.setRole(new ArrayList<>());

        user.getRoles().forEach(role -> userInfo.getRole().add(role.getName().name()));

        log.info("AuthService: user info retrieved successfully for user: {}", username);
        return userInfo;
    }

    public UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            log.info("AuthService: retrieving current user info for username: {}", username);
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                UserInfo userInfo = new UserInfo();
                userInfo.setId(user.getId());
                userInfo.setName(user.getName());
                userInfo.setSurname(user.getSurname());
                log.info("AuthService: current user info retrieved successfully for username: {}", username);
                return userInfo;
            }
        }
        log.warn("AuthService: no authenticated user found.");
        return null;
    }
}
