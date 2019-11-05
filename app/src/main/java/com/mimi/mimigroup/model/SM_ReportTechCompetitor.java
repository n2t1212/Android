package com.mimi.mimigroup.model;

public class SM_ReportTechCompetitor {
    String competitorId;
    String reportTechId;
    String title;
    String notes;
    String useful;
    String harmful;

    public SM_ReportTechCompetitor() {
    }

    public SM_ReportTechCompetitor(String competitorId, String reportTechId, String title, String notes, String useful, String harmful) {
        this.competitorId = competitorId;
        this.reportTechId = reportTechId;
        this.title = title;
        this.notes = notes;
        this.useful = useful;
        this.harmful = harmful;
    }

    public String getCompetitorId() {
        return competitorId;
    }

    public void setCompetitorId(String competitorId) {
        this.competitorId = competitorId;
    }

    public String getReportTechId() {
        return reportTechId;
    }

    public void setReportTechId(String reportTechId) {
        this.reportTechId = reportTechId;
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

    public String getUseful() {
        return useful;
    }

    public void setUseful(String useful) {
        this.useful = useful;
    }

    public String getHarmful() {
        return harmful;
    }

    public void setHarmful(String harmful) {
        this.harmful = harmful;
    }
}
