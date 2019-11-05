package com.mimi.mimigroup.model;

public class SM_ReportSaleRepMarket {
    String marketId;
    String reportSaleId;
    String customerId;
    String companyName;
    String productCode;
    String notes;
    Float price;

    public SM_ReportSaleRepMarket(String marketId, String reportSaleId, String customerId, String companyName, String productCode, String notes, Float price) {
        this.marketId = marketId;
        this.reportSaleId = reportSaleId;
        this.customerId = customerId;
        this.companyName = companyName;
        this.productCode = productCode;
        this.notes = notes;
        this.price = price;
    }

    public SM_ReportSaleRepMarket() {
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getReportSaleId() {
        return reportSaleId;
    }

    public void setReportSaleId(String reportSaleId) {
        this.reportSaleId = reportSaleId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
