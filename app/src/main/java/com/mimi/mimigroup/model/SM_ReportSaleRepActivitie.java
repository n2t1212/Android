package com.mimi.mimigroup.model;

public class SM_ReportSaleRepActivitie {
    String activitieId;
    String reportSaleId;
    String isType;
    String workDay;
    String title;
    String place;
    String notes;

    public SM_ReportSaleRepActivitie() {
    }

    public SM_ReportSaleRepActivitie(String activitieId, String reportSaleId, String isType, String workDay, String title, String place, String notes) {
        this.activitieId = activitieId;
        this.reportSaleId = reportSaleId;
        this.isType = isType;
        this.workDay = workDay;
        this.title = title;
        this.place = place;
        this.notes = notes;
    }

    public String getActivitieId() {
        return activitieId;
    }

    public void setActivitieId(String activitieId) {
        this.activitieId = activitieId;
    }

    public String getReportSaleId() {
        return reportSaleId;
    }

    public void setReportSaleId(String reportSaleId) {
        this.reportSaleId = reportSaleId;
    }

    public String getIsType() {
        return isType;
    }

    public void setIsType(String isType) {
        this.isType = isType;
    }

    public String getWorkDay() {
        return workDay;
    }

    public void setWorkDay(String workDay) {
        this.workDay = workDay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
