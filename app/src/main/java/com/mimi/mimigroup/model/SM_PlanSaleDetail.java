package com.mimi.mimigroup.model;

public class SM_PlanSaleDetail {
    String PlanDetailId;
    String PlanId;
    String CustomerId;
    String ProductCode;
    Double AmountBox;
    Double Amount;
    String Notes;
    String Notes2;

    public SM_PlanSaleDetail() {
    }

    public SM_PlanSaleDetail(String planDetailId, String planId, String customerId, String productCode, Double amountBox, Double amount, String notes, String notes2) {
        PlanDetailId = planDetailId;
        PlanId = planId;
        CustomerId = customerId;
        ProductCode = productCode;
        AmountBox = amountBox;
        Amount = amount;
        Notes = notes;
        Notes2 = notes2;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getPlanDetailId() {
        return PlanDetailId;
    }

    public void setPlanDetailId(String planDetailId) {
        PlanDetailId = planDetailId;
    }

    public String getPlanId() {
        return PlanId;
    }

    public void setPlanId(String planId) {
        PlanId = planId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public Double getAmountBox() {
        return AmountBox;
    }

    public void setAmountBox(Double amountBox) {
        AmountBox = amountBox;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getNotes2() {
        return Notes2;
    }

    public void setNotes2(String notes2) {
        Notes2 = notes2;
    }
}
