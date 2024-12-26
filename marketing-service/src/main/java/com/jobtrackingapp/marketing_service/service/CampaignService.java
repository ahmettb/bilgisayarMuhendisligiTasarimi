package com.jobtrackingapp.marketing_service.service;

import com.jobtrackingapp.marketing_service.entity.Campaign;
import com.jobtrackingapp.marketing_service.entity.MarketingUser;
import com.jobtrackingapp.marketing_service.entity.PermissionUser;
import com.jobtrackingapp.marketing_service.entity.User;
import com.jobtrackingapp.marketing_service.entity.constant.CampaignStatus;
import com.jobtrackingapp.marketing_service.entity.request.CreateCampaignRequest;
import com.jobtrackingapp.marketing_service.entity.request.CreatePermissionRequest;
import com.jobtrackingapp.marketing_service.entity.request.UpdateCampaignRequest;
import com.jobtrackingapp.marketing_service.entity.request.UpdateCampaignStatusRequest;
import com.jobtrackingapp.marketing_service.entity.response.ApiResponse;
import com.jobtrackingapp.marketing_service.entity.response.CampaignResponse;
import com.jobtrackingapp.marketing_service.entity.response.PermissionResponse;
import com.jobtrackingapp.marketing_service.exception.CampaignNotFoundException;
import com.jobtrackingapp.marketing_service.mapper.MapperEntity;
import com.jobtrackingapp.marketing_service.repository.ICampaignRepository;
import com.jobtrackingapp.marketing_service.repository.MarketingUserRepository;
import com.jobtrackingapp.marketing_service.repository.PermissionUserRepository;
import com.jobtrackingapp.marketing_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CampaignService {

    private final ICampaignRepository campaignRepository;

    private final UserRepository userRepository;

    private final MarketingUserRepository marketingUserRepository;

    private final PermissionUserRepository permissionUserRepository;

    public ApiResponse<Void> createPermission(CreatePermissionRequest request) {
        try {
            PermissionUser permissionUser = new PermissionUser();

            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User Not Found With ID" + request.getUserId()));

            permissionUser.setDayCount(request.getDayCount());
            permissionUser.setStartDateOfPermission(request.getStartDateOfPermission());
            permissionUser.setAccepted(false);
            permissionUser.setUser(user);
            permissionUserRepository.save(permissionUser);

            return ApiResponse.success("Permission Request Created Successfully", null);
        } catch (EntityNotFoundException e) {
            return ApiResponse.error("Error: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Error creating permission: " + e.getMessage());
        }
    }

    public ApiResponse<List<PermissionResponse>> getPermission(Long id) {
        try {
            List<PermissionUser> permissionUser = permissionUserRepository.findByUser_Id(id)
                    .orElseThrow(() -> new EntityNotFoundException("Permissions not found for User ID: " + id));

            List<PermissionResponse> responseList = new ArrayList<>();
            for (PermissionUser permissionUser1 : permissionUser) {
                PermissionResponse permissionResponse = new PermissionResponse();
                permissionResponse.setSurname(permissionUser1.getUser().getSurname());
                permissionResponse.setUserName(permissionUser1.getUser().getName());
                permissionResponse.setUserId(permissionUser1.getUser().getId());
                permissionResponse.setStartDateOfPermission(permissionUser1.getStartDateOfPermission());
                permissionResponse.setDayCount(permissionUser1.getDayCount());
                permissionResponse.setAccepted(permissionUser1.getAccepted());
                permissionResponse.setPermissionId(permissionUser1.getId());

                responseList.add(permissionResponse);
            }

            return ApiResponse.success("Permission List Retrieved Successfully", responseList);
        } catch (EntityNotFoundException e) {
            return ApiResponse.error("Error: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving permissions: " + e.getMessage());
        }
    }

    @Transactional
    public ApiResponse<Void> saveCampaign(CreateCampaignRequest request) {
        try {
            User user = userRepository.findById(request.getCreatedUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Marketing User Not Found With ID" + request.getCreatedUserId()));

            Campaign campaign = new Campaign();
            campaign.setName(request.getName());
            campaign.setStatus(CampaignStatus.ACTIVE);
            campaign.setDescription(request.getDescription());
            campaign.setBudget(request.getBudget());
            campaign.setTargetReach(request.getTargetReach());
            campaign.setStartDate(request.getStartDate());
            campaign.setEndDate(request.getEndDate());

            MarketingUser marketingUser = marketingUserRepository.findMarketingUserByUserId(request.getCreatedUserId())
                    .orElseGet(() -> {
                        MarketingUser newMarketingUser = new MarketingUser();
                        newMarketingUser.setUser(user);
                        return marketingUserRepository.save(newMarketingUser);
                    });

            campaign.setUser(marketingUser);
            campaignRepository.save(campaign);

            return ApiResponse.success("Campaign Created Successfully", null);
        } catch (EntityNotFoundException e) {
            return ApiResponse.error("Error: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Error creating campaign: " + e.getMessage());
        }
    }

    @Cacheable(value = "campaigns", key = "#id")
    public ApiResponse<CampaignResponse> getCampaignById(long id) {
        try {
            Campaign campaign = campaignRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new CampaignNotFoundException("Campaign Not Found"));

            CampaignResponse campaignResponse = MapperEntity.mapToCampaignFromRequest(campaign);
            return ApiResponse.success("Campaign Retrieved Successfully", campaignResponse);
        } catch (CampaignNotFoundException e) {
            return ApiResponse.error("Error: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving campaign: " + e.getMessage());
        }
    }

    public ApiResponse<List<CampaignResponse>> getAllCampaign() {
        try {
            List<Campaign> campaignList = campaignRepository.findAll();
            List<CampaignResponse> campaignResponse = MapperEntity.mapToCampaignFromRequest(campaignList);
            return ApiResponse.success("All Campaigns Retrieved Successfully", campaignResponse);
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving campaigns: " + e.getMessage());
        }
    }

    public ApiResponse<Void> deleteCampaignById(long id) {
        try {
            Campaign campaign = campaignRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new CampaignNotFoundException("Campaign Not Found"));

            campaign.setDeleted(true);
            campaignRepository.save(campaign);

            return ApiResponse.success("Campaign Deleted Successfully", null);
        } catch (CampaignNotFoundException e) {
            return ApiResponse.error("Error: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Error deleting campaign: " + e.getMessage());
        }
    }

    public ApiResponse<Void> updateCampaign(UpdateCampaignRequest request) {
        try {
            Campaign campaign = campaignRepository.findByIdAndDeletedFalse(request.getId())
                    .orElseThrow(() -> new CampaignNotFoundException("Campaign Not Found"));

            campaign.setUsedBudget(request.getUsedBudget());
            campaign.setTargetReach(request.getTargetReach());
            campaign.setName(request.getName());
            campaign.setDescription(request.getDescription());
            campaign.setStartDate(request.getStartDate());
            campaign.setEndDate(request.getEndDate());
            campaign.setBudget(request.getBudget());
            CampaignStatus campaignStatus = CampaignStatus.fromFormattedString(request.getStatus());
            campaign.setStatus(campaignStatus);

            campaignRepository.save(campaign);

            return ApiResponse.success("Campaign Updated Successfully", null);
        } catch (CampaignNotFoundException e) {
            return ApiResponse.error("Error: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Error updating campaign: " + e.getMessage());
        }
    }

    public ApiResponse<Void> updateCampaignStatus(UpdateCampaignStatusRequest request) {
        try {
            Campaign campaign = campaignRepository.findByIdAndDeletedFalse(request.getId())
                    .orElseThrow(() -> new CampaignNotFoundException("Campaign Not Found"));

            CampaignStatus campaignStatus = CampaignStatus.fromFormattedString(request.getStatus());
            campaign.setStatus(campaignStatus);

            campaignRepository.save(campaign);

            return ApiResponse.success("Campaign Status Updated Successfully", null);
        } catch (CampaignNotFoundException e) {
            return ApiResponse.error("Error: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Error updating campaign status: " + e.getMessage());
        }
    }
}