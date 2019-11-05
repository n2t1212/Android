package com.mimi.mimigroup.model;

public class SM_ReportTechMarket {
    String marketId;
    String reportTechId;
    String title;
    String notes;
    String usefull;
    String harmful;

    public SM_ReportTechMarket() {
    }

    public SM_ReportTechMarket(String marketId, String reportTechId, String title, String notes, String usefull, String harmful) {
        this.marketId = marketId;
        this.reportTechId = reportTechId;
        this.title = title;
        this.notes = notes;
        this.usefull = usefull;
        this.harmful = harmful;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
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

    public String getUsefull() {
        return usefull;
    }

    public void setUsefull(String usefull) {
        this.usefull = usefull;
    }

    public String getHarmful() {
        return harmful;
    }

    public void setHarmful(String harmful) {
        this.harmful = harmful;
    }
}
