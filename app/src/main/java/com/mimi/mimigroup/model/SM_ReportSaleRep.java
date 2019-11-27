package com.mimi.mimigroup.model;

public class SM_ReportSaleRep {
    String reportSaleId;
    String reportCode;
    String reportName;
    String reportDay;
    Float longtitude;
    Float latitude;
    String locationAddress;
    String receiverList;
    String notes;
    Integer isStatus;
    String IsStatusDesc;
    Boolean isPost;
    String postDay;

    public SM_ReportSaleRep() {}
    public SM_ReportSaleRep(String reportSaleId, String reportCode, String reportName, String reportDay, Float longtitude, Float latitude, String locationAddress, String receiverList, String notes, Integer isStatus, Boolean isPost, String postDay) {
        this.reportSaleId = reportSaleId;
        this.reportCode = reportCode;
        this.reportName = reportName;
        this.reportDay = reportDay;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.locationAddress = locationAddress;
        this.receiverList = receiverList;
        this.notes = notes;
        this.isStatus = isStatus;
        this.isPost = isPost;
        this.postDay = postDay;
    }


    public String getReportSaleId() {
        return reportSaleId;
    }

    public void setReportSaleId(String reportSaleId) {
        this.reportSaleId = reportSaleId;
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

    public String getReportDay() {
        return reportDay;
    }

    public void setReportDay(String reportDay) {
        this.reportDay = reportDay;
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

    public String getIsStatusDesc() {
        return IsStatusDesc;
    }

    public void setIsStatusDesc(String isStatusDesc) {
        IsStatusDesc = isStatusDesc;
    }

    public Boolean getPost() {
        return isPost;
    }

    public void setPost(Boolean post) {
        isPost = post;
    }

    public String getPostDay() {
        return postDay;
    }

    public void setPostDay(String postDay) {
        this.postDay = postDay;
    }


}
