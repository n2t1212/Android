package com.mimi.mimigroup.model;

public class SM_OrderDetail {
    String OrderDetailID;
    String OrderID;
    String ProductID;
    String ProductCode;
    String ProductName;
    String Unit;
    String Spec;
    Float ConvertBox;
    Integer Amount;
    Float AmountBox;
    Double Price;
    Double OriginMoney;
    String Notes;

    //TreeList;
    String Notes2;

    public SM_OrderDetail() {
    }

    public SM_OrderDetail(String orderDetailId,String orderId,String productId,String productCode,String productName,String unit,
                          String spec,Integer amount,Double price,Double originMoney,String notes,String notes2) {

      this.OrderDetailID=orderDetailId;
      this.OrderID=orderId;
      this.ProductID=productId;
      this.ProductCode=productCode;
      this.ProductName=productName;
      this.Unit=unit;
      this.Spec=spec;
      this.Amount=amount;
      this.Price=price;
      this.OriginMoney=originMoney;
      this.Notes=notes;
      this.Notes2=notes2;
    }

    public String getOrderDetailID() {
        return OrderDetailID;
    }

    public void setOrderDetailID(String orderDetailID) {
        OrderDetailID = orderDetailID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
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

    public Float getConvertBox() {
        return ConvertBox;
    }

    public void setConvertBox(Float convertBox) {
        ConvertBox = convertBox;
    }

    public Double getOriginMoney() {
        return OriginMoney;
    }

    public void setOriginMoney(Double originMoney) {
        OriginMoney = originMoney;
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

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
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
