package com.jobtrackingapp.hr_service.service;



import com.jobtrackingapp.hr_service.entity.PermissionUser;
import com.jobtrackingapp.hr_service.entity.User;
import com.jobtrackingapp.hr_service.entity.request.CreatePermissionRequest;
import com.jobtrackingapp.hr_service.entity.response.ApiResponse;
import com.jobtrackingapp.hr_service.entity.response.PermissionResponse;
import com.jobtrackingapp.hr_service.repository.PermissionUserRepository;
import com.jobtrackingapp.hr_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class PermissionService {



    private final PermissionUserRepository permissionUserRepository;
    private final UserRepository userRepository;

    public ApiResponse<Void> acceptPermission(Long id) {
        log.info("acceptPermission method started for permission ID: {}", id);
        PermissionUser permissionUser = permissionUserRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Permission not found with ID: {}", id);
                    return new EntityNotFoundException("Permission not found with ID " + id);
                });

        permissionUser.setAccepted(true);
        permissionUserRepository.save(permissionUser);

        log.info("acceptPermission method successfully completed for permission ID: {}", id);
        return ApiResponse.success("Permission accepted", null);
    }

    public ApiResponse<Void> rejectPermission(Long id) {
        log.info("acceptPermission method started for permission ID: {}", id);
        PermissionUser permissionUser = permissionUserRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Permission not found with ID: {}", id);
                    return new EntityNotFoundException("Permission not found with ID " + id);
                });

        permissionUser.setAccepted(false);
        permissionUserRepository.save(permissionUser);

        log.info("acceptPermission method successfully completed for permission ID: {}", id);
        return ApiResponse.success("Permission accepted", null);
    }



    public ApiResponse<String> createPermission(CreatePermissionRequest request) {
        try {
            PermissionUser permissionUser = new PermissionUser();
            User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("User Not Found With ID" + request.getUserId()));

            permissionUser.setDayCount(request.getDayCount());
            permissionUser.setStartDateOfPermission(request.getStartDateOfPermission());
            permissionUser.setAccepted(false);
            permissionUser.setUser(user);
            permissionUserRepository.save(permissionUser);

            return ApiResponse.success("Permission Request Success", null);
        } catch (EntityNotFoundException ex) {
            return ApiResponse.error("User not found: " + ex.getMessage());
        } catch (Exception ex) {
            return ApiResponse.error("Error while creating permission: " + ex.getMessage());
        }
    }

    public ApiResponse<Page<PermissionResponse>> getPermission(Long id, Pageable pageable) {
        try {
            Page<PermissionUser> permissionUserPage = permissionUserRepository.findByUser_Id(id, pageable);

            List<PermissionResponse> responseList = permissionUserPage.getContent().stream()
                    .map(permissionUser1 -> {
                        PermissionResponse permissionResponse = new PermissionResponse();
                        permissionResponse.setSurname(permissionUser1.getUser().getSurname());
                        permissionResponse.setUserName(permissionUser1.getUser().getName());
                        permissionResponse.setUserId(permissionUser1.getUser().getId());
                        permissionResponse.setStartDateOfPermission(permissionUser1.getStartDateOfPermission());
                        permissionResponse.setDayCount(permissionUser1.getDayCount());
                        permissionResponse.setAccepted(permissionUser1.getAccepted());
                        permissionResponse.setPermissionId(permissionUser1.getId());
                        return permissionResponse;
                    })
                    .collect(Collectors.toList());

            Page<PermissionResponse> permissionResponsePage = new PageImpl<>(responseList, pageable, permissionUserPage.getTotalElements());

            return ApiResponse.success("Get Permission Success", permissionResponsePage);
        } catch (EntityNotFoundException ex) {
            return ApiResponse.error("Permissions not found: " + ex.getMessage());
        } catch (Exception ex) {
            return ApiResponse.error("Error while fetching permissions: " + ex.getMessage());
        }
    }


    public ApiResponse<Void> deletePermission(Long id) {
        try {

         PermissionUser permissionUser=   permissionUserRepository.findById(id).orElseThrow(()->new EntityNotFoundException());

         permissionUserRepository.delete(permissionUser);

            return ApiResponse.success("Delete Permission Success", null);
        } catch (EntityNotFoundException ex) {
            return ApiResponse.error("Permissions not found: " + ex.getMessage());
        } catch (Exception ex) {
            return ApiResponse.error("Error while fetching permissions: " + ex.getMessage());
        }
    }
}
