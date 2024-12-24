package com.jobtrackingapp.marketing_service.mapper;

import com.jobtrackingapp.marketing_service.entity.Campaign;
import com.jobtrackingapp.marketing_service.entity.response.CampaignResponse;

import java.util.ArrayList;
import java.util.List;

public class MapperEntity {


    public static CampaignResponse mapToCampaignFromRequest(Campaign campaign) {

        return CampaignResponse.builder().name(campaign.getName()).endDate(campaign.getEndDate()).startDate(campaign.getStartDate())
                .budget(campaign.getBudget()).usedBudget(campaign.getUsedBudget()).targetReach(campaign.getTargetReach())
                .status(campaign.getStatus().name()).description(campaign.getDescription())
                .userId(campaign.getUser().getId()).userName(campaign.getUser().getUser().getName()).userSurname(campaign.getUser().getUser().getSurname())
                .build();



    }

    public static List<CampaignResponse> mapToCampaignFromRequest(List<Campaign> campaign) {

        List<CampaignResponse> campaignResponseList = new ArrayList<>();

        for (Campaign campaign1 : campaign) {

            campaignResponseList.add(mapToCampaignFromRequest(campaign1));
        }
        return campaignResponseList;
    }
}
