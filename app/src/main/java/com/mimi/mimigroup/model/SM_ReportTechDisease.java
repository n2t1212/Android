package com.mimi.mimigroup.model;

public class SM_ReportTechDisease {
    String diseaseId;
    String reportTechId;
    String treeCode;
    String stagesCode;
    String title;
    Float acreage;
    String disease;
    Float price;
    String notes;

    public SM_ReportTechDisease() {
    }

    public SM_ReportTechDisease(String diseaseId, String reportTechId, String treeCode, String stagesCode, String title, Float acreage, String disease, Float price, String notes) {
        this.diseaseId = diseaseId;
        this.reportTechId = reportTechId;
        this.treeCode = treeCode;
        this.stagesCode = stagesCode;
        this.title = title;
        this.acreage = acreage;
        this.disease = disease;
        this.price = price;
        this.notes = notes;
    }

    public String getStagesCode() {
        return stagesCode;
    }

    public void setStagesCode(String stagesCode) {
        this.stagesCode = stagesCode;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getReportTechId() {
        return reportTechId;
    }

    public void setReportTechId(String reportTechId) {
        this.reportTechId = reportTechId;
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

    public Float getAcreage() {
        return acreage;
    }

    public void setAcreage(Float acreage) {
        this.acreage = acreage;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
