package com.mimi.mimigroup.model;

public class SM_ReportSaleRepSeason {
    String seasonId;
    String reportSaleId;
    String treeCode;
    String seasonCode;
    String title;
    Float acreage;
    String notes;

    public SM_ReportSaleRepSeason() {
    }

    public SM_ReportSaleRepSeason(String seasonId, String reportSaleId, String treeCode, String seasonCode, String title, Float acreage, String notes) {
        this.seasonId = seasonId;
        this.reportSaleId = reportSaleId;
        this.treeCode = treeCode;
        this.seasonCode = seasonCode;
        this.title = title;
        this.acreage = acreage;
        this.notes = notes;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getReportSaleId() {
        return reportSaleId;
    }

    public void setReportSaleId(String reportSaleId) {
        this.reportSaleId = reportSaleId;
    }

    public String getTreeCode() {
        return treeCode;
    }

    public void setTreeCode(String treeCode) {
        this.treeCode = treeCode;
    }

    public String getSeasonCode() {
        return seasonCode;
    }

    public void setSeasonCode(String seasonCode) {
        this.seasonCode = seasonCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getAcreage() {
        return acreage;
    }

    public void setAcreage(Float acreage) {
        this.acreage = acreage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
