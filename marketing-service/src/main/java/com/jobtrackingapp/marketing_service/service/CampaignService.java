package com.jobtrackingapp.marketing_service.service;

import com.jobtrackingapp.marketing_service.entity.Campaign;
import com.jobtrackingapp.marketing_service.entity.constant.CampaignStatus;
import com.jobtrackingapp.marketing_service.entity.request.CreateCampaignRequest;
import com.jobtrackingapp.marketing_service.entity.request.UpdateCampaignRequest;
import com.jobtrackingapp.marketing_service.entity.request.UpdateCampaignStatusRequest;
import com.jobtrackingapp.marketing_service.entity.response.CampaignResponse;
import com.jobtrackingapp.marketing_service.exception.CampaignNotFoundException;
import com.jobtrackingapp.marketing_service.mapper.MapperEntity;
import com.jobtrackingapp.marketing_service.repository.ICampaignRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CampaignService {

    private final ICampaignRepository campaignRepository;


    @Transactional
    public void saveCampaign(CreateCampaignRequest request) {

        Campaign campaign = new Campaign();

        campaign.setName(request.getName());
        campaign.setStatus(CampaignStatus.ACTIVE);
        campaign.setDescription(request.getDescription());
        campaign.setBudget(request.getBudget());
        campaign.setTargetReach(request.getTargetReach());
        campaign.setStartDate(request.getStartDate());
        campaign.setEndDate(request.getEndDate());
        campaignRepository.save(campaign);

    }

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
