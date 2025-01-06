package com.jobtrackingapp.marketing_service.entity.constant;

public enum CampaignStatus {
    ACTIVE,
    INACTIVE,
    PENDING,
    COMPLETED;  // Yeni eklenen durum

    public static CampaignStatus fromFormattedString(String status) {
        switch (status.toUpperCase()) {
            case "ACTIVE":
                return ACTIVE;
            case "INACTIVE":
                return INACTIVE;
            case "PENDING":
                return PENDING;
            case "COMPLETED":  // Yeni durum için case ekleme
                return COMPLETED;
            default:
                throw new IllegalArgumentException("Invalid status: " + status);
        }
    }
}
