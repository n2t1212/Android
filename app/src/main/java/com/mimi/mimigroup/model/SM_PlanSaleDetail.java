package com.mimi.mimigroup.model;

public class SM_PlanSaleDetail {
    String PlanDetailId;
    String PlanId;
    String CustomerId;
    String CustomerCode;
    String CustomerName;
    String ProductCode;
    String ProductName;
    String Unit;
    String Spec;
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

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getSpec() {
        return Spec;
    }

    public void setSpec(String spec) {
        Spec = spec;
    }

    public String getUnit() {
        return Unit;
    }
    public void setUnit(String unit) {
        Unit = unit;
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
