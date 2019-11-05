package com.mimi.mimigroup.model;

public class SM_ReportTech {
    String reportTechId;
    String reportCode;
    String reportName;
    String reportDate;
    Float longtitude;
    Float latitude;
    String locationAddress;
    String receiverList;
    String notes;
    Integer isStatus;
    Boolean isPost;
    String postDate;

    public SM_ReportTech() {
    }

    public SM_ReportTech(String reportTechId, String reportCode, String reportName, String reportDate, Float longtitude, Float latitude, String locationAddress, String receiverList, String notes, Integer isStatus, Boolean isPost, String postDate) {
        this.reportTechId = reportTechId;
        this.reportCode = reportCode;
        this.reportName = reportName;
        this.reportDate = reportDate;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.locationAddress = locationAddress;
        this.receiverList = receiverList;
        this.notes = notes;
        this.isStatus = isStatus;
        this.isPost = isPost;
        this.postDate = postDate;
    }

    public String getReportTechId() {
        return reportTechId;
    }

    public void setReportTechId(String reportTechId) {
        this.reportTechId = reportTechId;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public Float getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Float longtitude) {
        this.longtitude = longtitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(String receiverList) {
        this.receiverList = receiverList;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(Integer isStatus) {
        this.isStatus = isStatus;
    }

    public Boolean isPost() {
        return isPost;
    }

    public void setPost(Boolean post) {
        isPost = post;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }
}
