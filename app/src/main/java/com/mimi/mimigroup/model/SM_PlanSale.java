package com.mimi.mimigroup.model;

public class SM_PlanSale {
    String PlanId;
    String PlanCode;
    String PlanDay;
    String StartDay;
    String EndDay;
    String PlanName;
    String PostDay;
    Boolean IsPost;
    String IsStatus;
    String Notes;

    public SM_PlanSale() {
    }

    public SM_PlanSale(String planId, String planCode, String planDay, String startDay, String endDay, String planName, String postDay, Boolean isPost, String isStatus, String notes) {
        PlanId = planId;
        PlanCode = planCode;
        PlanDay = planDay;
        StartDay = startDay;
        EndDay = endDay;
        PlanName = planName;
        PostDay = postDay;
        IsPost = isPost;
        IsStatus = isStatus;
        Notes = notes;
    }

    public String getPlanId() {
        return PlanId;
    }

    public void setPlanId(String planId) {
        PlanId = planId;
    }

    public String getPlanCode() {
        return PlanCode;
    }

    public void setPlanCode(String planCode) {
        PlanCode = planCode;
    }

    public String getPlanDay() {
        return PlanDay;
    }

    public void setPlanDay(String planDay) {
        PlanDay = planDay;
    }

    public String getStartDay() {
        return StartDay;
    }

    public void setStartDay(String startDay) {
        StartDay = startDay;
    }

    public String getEndDay() {
        return EndDay;
    }

    public void setEndDay(String endDay) {
        EndDay = endDay;
    }

    public String getPlanName() {
        return PlanName;
    }

    public void setPlanName(String planName) {
        PlanName = planName;
    }

    public String getPostDay() {
        return PostDay;
    }

    public void setPostDay(String postDay) {
        PostDay = postDay;
    }

    public Boolean getPost() {
        return IsPost;
    }

    public void setPost(Boolean post) {
        IsPost = post;
    }

    public String getIsStatus() {
        return IsStatus;
    }

    public void setIsStatus(String isStatus) {
        IsStatus = isStatus;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }
}
