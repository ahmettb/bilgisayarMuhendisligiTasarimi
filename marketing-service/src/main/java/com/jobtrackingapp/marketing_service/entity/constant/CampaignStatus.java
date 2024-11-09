package com.jobtrackingapp.marketing_service.entity.constant;

public enum CampaignStatus {

    ACTIVE("Active"),
    COMPLETED("Completed"),
    CANCELED("Canceled");

    private final String formattedString;

    CampaignStatus(String formattedString) {
        this.formattedString = formattedString;
    }

    public String getFormattedString() {
        return formattedString;
    }



    public static CampaignStatus fromFormattedString(String formattedString) {
        for (CampaignStatus type : values()) {
            if (type.getFormattedString().equalsIgnoreCase(formattedString)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Geçersiz cevap tipi: " + formattedString);
    }

}
