package com.mimi.mimigroup.model;

public class SM_ReportTechActivity {
    String activitieId;
    String reportTechId;
    Integer isType;
    String title;
    String notes;
    String achievement;

    public SM_ReportTechActivity() {
    }

    public SM_ReportTechActivity(String activitieId, String reportTechId, Integer isType, String title, String notes, String achievement) {
        this.activitieId = activitieId;
        this.reportTechId = reportTechId;
        this.isType = isType;
        this.title = title;
        this.notes = notes;
        this.achievement = achievement;
    }

    public String getActivitieId() {
        return activitieId;
    }

    public void setActivitieId(String activitieId) {
        this.activitieId = activitieId;
    }

    public String getReportTechId() {
        return reportTechId;
    }

    public void setReportTechId(String reportTechId) {
        this.reportTechId = reportTechId;
    }

    public Integer getIsType() {
        return isType;
    }

    public void setIsType(Integer isType) {
        this.isType = isType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }
}
