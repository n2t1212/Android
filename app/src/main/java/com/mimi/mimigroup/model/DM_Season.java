package com.mimi.mimigroup.model;

public class DM_Season {
    Integer SeasonID;
    String SeasonCode;
    String SeasonName;

    public DM_Season() {
    }

    public DM_Season(Integer seasonID,String seasonCode, String seasonName) {
        this.SeasonID=seasonID;
        this.SeasonCode = seasonCode;
        this.SeasonName = seasonName;
    }

    public Integer getSeasonID() {
        return SeasonID;
    }

    public void setSeasonID(Integer seasonID) {
        SeasonID = seasonID;
    }

    public String getSeasonCode() {
        return SeasonCode;
    }

    public void setSeasonCode(String seasonCode) {
        this.SeasonCode = seasonCode;
    }

    public String getSeasonName() {
        return SeasonName;
    }

    public void setSeasonName(String seasonName) {
        this.SeasonName = seasonName;
    }
}
