package com.mimi.mimigroup.model;

public class SM_ReportSaleRepDisease {
    String diseaseId;
    String reportSaleId;
    String treeCode;
    String title;
    Float arceage;
    String notes;

    public SM_ReportSaleRepDisease() {
    }

    public SM_ReportSaleRepDisease(String diseaseId, String reportSaleId, String treeCode, String title, Float arceage, String notes) {
        this.diseaseId = diseaseId;
        this.reportSaleId = reportSaleId;
        this.treeCode = treeCode;
        this.title = title;
        this.arceage = arceage;
        this.notes = notes;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getArceage() {
        return arceage;
    }

    public void setArceage(Float arceage) {
        this.arceage = arceage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
