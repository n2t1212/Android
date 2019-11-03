package com.mimi.mimigroup.model;

public class SM_OrderDeliveryDetail {
    String DeliveryOrderDetailID;
    String DeliveryOrderID;
    String ProductCode;
    String ProductName;
    String Unit;
    String Spec;
    Integer Amount;
    Float AmountBox;
    Float Price;
    Float OriginMoney;
    String Notes;
    public SM_OrderDeliveryDetail() {}

    public SM_OrderDeliveryDetail(String deliveryOrderDetailID,String deliveryOrderID,String productCode,String productName,
                                  String unit,String spec,Integer amount,Float price,Float originMoney,String notes) {

       this.DeliveryOrderDetailID=deliveryOrderDetailID;
       this.DeliveryOrderID=deliveryOrderID;
       this.ProductCode=productCode;
       this.ProductName=productName;
       this.Unit=unit;
       this.Spec=spec;
       this.Amount=amount;
       this.Price=price;
       this.OriginMoney=originMoney;
       this.Notes=notes;
    }

    public String getDeliveryOrderDetailID() {
        return DeliveryOrderDetailID;
    }

    public void setDeliveryOrderDetailID(String deliveryOrderDetailID) {
        DeliveryOrderDetailID = deliveryOrderDetailID;
    }

    public String getDeliveryOrderID() {
        return DeliveryOrderID;
    }

    public void setDeliveryOrderID(String deliveryOrderID) {
        DeliveryOrderID = deliveryOrderID;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }
    public String getProductName() {
        return ProductName;
    }

    public String getUnit() {
        return Unit;
    }
    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getSpec() {
        return Spec;
    }

    public void setSpec(String spec) {
        Spec = spec;
    }

    public Integer getAmount() {
        return Amount;
    }

    public void setAmount(Integer amount) {
        Amount = amount;
    }

    public Float getAmountBox() {
        return AmountBox;
    }

    public void setAmountBox(Float amountBox) {
        AmountBox = amountBox;
    }

    public Float getPrice() {
        return Price;
    }

    public void setPrice(Float price) {
        Price = price;
    }

    public Float getOriginMoney() {
        return OriginMoney;
    }

    public void setOriginMoney(Float originMoney) {
        OriginMoney = originMoney;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }
}
