package com.mimi.mimigroup.model;

public class DM_Season {
    String seasonCode;
    String seasonName;

    public DM_Season() {
    }

    public DM_Season(String seasonCode, String seasonName) {
        this.seasonCode = seasonCode;
        this.seasonName = seasonName;
    }

    public String getSeasonCode() {
        return seasonCode;
    }

    public void setSeasonCode(String seasonCode) {
        this.seasonCode = seasonCode;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }
}
