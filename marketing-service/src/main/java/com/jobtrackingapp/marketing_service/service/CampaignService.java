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

    private  final MarketingUserRepository marketingUserRepository;

    private  final PermissionUserRepository permissionUserRepository;

    public void createPermission(CreatePermissionRequest request) {
        PermissionUser permissionUser = new PermissionUser();

        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("User Not Found With ID" + request.getUserId()));

        permissionUser.setDayCount(request.getDayCount());
        permissionUser.setStartDateOfPermission(request.getStartDateOfPermission());
        permissionUser.setAccepted(false);
        permissionUser.setUser(user);
        permissionUserRepository.save(permissionUser);


    }

    public List<PermissionResponse> getPermission(Long id)
    {
        List<PermissionUser> permissionUser= permissionUserRepository.findByUser_Id(id).orElseThrow(()->new EntityNotFoundException());

        List<PermissionResponse> responseList=new ArrayList<>();
        for(PermissionUser permissionUser1:permissionUser)
        {
            PermissionResponse permissionResponse=new PermissionResponse();
            permissionResponse.setSurname(permissionUser1.getUser().getSurname());
            permissionResponse.setUserName(permissionUser1.getUser().getName());
            permissionResponse.setUserId(permissionUser1.getUser().getId());
            permissionResponse.setStartDateOfPermission(permissionUser1.getStartDateOfPermission());
            permissionResponse.setDayCount(permissionUser1.getDayCount());
            permissionResponse.setAccepted(permissionUser1.getAccepted());
            permissionResponse.setPermissionId(permissionUser1.getId());

            responseList.add(permissionResponse);
        }


        return responseList;

    }


    @Transactional
    public void saveCampaign(CreateCampaignRequest request) {

     User user=userRepository.findById(request.getCreatedUserId()).orElseThrow(()->new EntityNotFoundException("Marketing User Not Found With ID" + request.getCreatedUserId()));


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

    }

    @Cacheable(value = "campaigns",key = "#id")
    public CampaignResponse getCampaignById(long id) {

        Campaign campaign = campaignRepository.findByIdAndDeletedFalse(id).orElseThrow(
                () -> new CampaignNotFoundException("Campaign Not Found")
        );
        CampaignResponse campaignResponse = MapperEntity.mapToCampaignFromRequest(campaign);
        return campaignResponse;

    }

    public List<CampaignResponse> getAllCampaign() {

        List<Campaign> campaignList = campaignRepository.findAll();
        List<CampaignResponse> campaignResponse = MapperEntity.mapToCampaignFromRequest(campaignList);
        return campaignResponse;
    }

    public void deleteCampaignById(long id) {
        Campaign campaign = campaignRepository.findByIdAndDeletedFalse(id).orElseThrow(
                () -> new CampaignNotFoundException("Campaign Not Found")
        );
        campaign.setDeleted(true);
        campaignRepository.save(campaign);

    }

    public void updateCampaign(UpdateCampaignRequest request) {
        Campaign campaign = campaignRepository.findByIdAndDeletedFalse(request.getId()).orElseThrow(
                () -> new CampaignNotFoundException("Campaign Not Found")
        );

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
    }

    public void updateCampaignStatus(UpdateCampaignStatusRequest request) {
        Campaign campaign = campaignRepository.findByIdAndDeletedFalse(request.getId()).orElseThrow(
                () -> new CampaignNotFoundException("Campaign Not Found")
        );
        CampaignStatus campaignStatus = CampaignStatus.fromFormattedString(request.getStatus());
        campaign.setStatus(campaignStatus);
        campaignRepository.save(campaign);
    }


}
