package com.jobtrackingapp.marketing_service.service;

import com.jobtrackingapp.marketing_service.entity.*;
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
import com.jobtrackingapp.marketing_service.repository.PermissionUserRepository;
import com.jobtrackingapp.marketing_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CampaignService {

    private final ICampaignRepository campaignRepository;

    private final UserRepository userRepository;


    @Transactional
    public ApiResponse<Void> saveCampaign(CreateCampaignRequest request) {
        try {
            User user = userRepository.findById(request.getCreatedUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User Not Found With ID" + request.getCreatedUserId()));

            user.getRoles().forEach(role -> {

                if(role.getName()==ERole.MARKETING)
                {
                   return;
                }
                throw new EntityNotFoundException("User Role Not Marketing");
            });

            Campaign campaign = new Campaign();
            campaign.setName(request.getName());
            campaign.setStatus(CampaignStatus.ACTIVE);
            campaign.setDescription(request.getDescription());
            campaign.setBudget(request.getBudget());
            campaign.setTargetReach(request.getTargetReach());
            campaign.setStartDate(request.getStartDate());
            campaign.setEndDate(request.getEndDate());


            campaign.setUser(user);
            campaignRepository.save(campaign);

            return ApiResponse.success("Campaign Created Successfully", null);
        } catch (EntityNotFoundException e) {
            return ApiResponse.error("Error: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Error creating campaign: " + e.getMessage());
        }
    }

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

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateCampaignStatuses() {
        List<Campaign> campaigns = campaignRepository.findAllByEndDateBeforeAndStatusNotIn(new Date(), List.of(CampaignStatus.COMPLETED));

        for (Campaign campaign : campaigns) {
            if (campaign.getEndDate().before(new Date())) {
                campaign.setStatus(CampaignStatus.COMPLETED);
                campaignRepository.save(campaign);
            }
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
    public ApiResponse<Void> updateCampaignStatusToCompleted(Long campaignId) {
        try {
            Campaign campaign = campaignRepository.findByIdAndDeletedFalse(campaignId)
                    .orElseThrow(() -> new CampaignNotFoundException("Campaign Not Found"));

            if (campaign.getEndDate().before(new Date())) {
                campaign.setStatus(CampaignStatus.COMPLETED);
                campaignRepository.save(campaign);
                return ApiResponse.success("Campaign Status Updated to COMPLETED", null);
            } else {
                return ApiResponse.error("Campaign end date is not reached yet.");
            }
        } catch (CampaignNotFoundException e) {
            return ApiResponse.error("Error: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Error updating campaign status: " + e.getMessage());
        }
    }

    public ApiResponse<Void> updateCampaign(Long id,UpdateCampaignRequest request) {
        try {
            Campaign campaign = campaignRepository.findByIdAndDeletedFalse(id)
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