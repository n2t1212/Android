package com.mimi.mimigroup.model;

public class SM_OrderDelivery {
    String DeliveryOrderID;
    String OrderID;
    String TransportCode;
    String NumberPlate;
    String CarType;
    String DeliveryStaff;
    Integer DeliveryNum;
    String DeliveryDate;
    String HandlingStaff;
    String HandlingDate;
    Float TotalMoney;
    String DeliveryDesc;



    public SM_OrderDelivery() {
    }

    public SM_OrderDelivery(String deliveryOrderID,String orderID,String transportCode,String numberPlate,String carType,String deliveryStaff,
                            Integer deliveryNum,String deliveryDate,String handlingStaff,String handlingDate,String deliveryDesc) {

      this.DeliveryOrderID=deliveryOrderID;
      this.OrderID=orderID;
      this.TransportCode=transportCode;
      this.NumberPlate=numberPlate;
      this.CarType=carType;
      this.DeliveryStaff=deliveryStaff;
      this.DeliveryNum=deliveryNum;
      this.DeliveryDate=deliveryDate;
      this.HandlingStaff=handlingStaff;
      this.HandlingDate=handlingDate;
      this.DeliveryDesc=deliveryDesc;
    }

    public String getDeliveryOrderID() {
        return DeliveryOrderID;
    }
    public void setDeliveryOrderID(String deliveryOrderID) {
        DeliveryOrderID = deliveryOrderID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getTransportCode() {
        return TransportCode;
    }

    public void setTransportCode(String transportCode) {
        TransportCode = transportCode;
    }

    public String getNumberPlate() {
        return NumberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        NumberPlate = numberPlate;
    }

    public String getCarType() {
        return CarType;
    }

    public void setCarType(String carType) {
        CarType = carType;
    }

    public Integer getDeliveryNum() {
        return DeliveryNum;
    }
    public void setDeliveryNum(Integer deliveryNum) {
        DeliveryNum = deliveryNum;
    }

    public String getDeliveryStaff() {
        return DeliveryStaff;
    }

    public void setDeliveryStaff(String deliveryStaff) {
        DeliveryStaff = deliveryStaff;
    }

    public String getHandlingStaff() {
        return HandlingStaff;
    }
    public void setHandlingStaff(String handlingStaff) {
        HandlingStaff = handlingStaff;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getHandlingDate() {
        return HandlingDate;
    }

    public void setHandlingDate(String handlingDate) {
        HandlingDate = handlingDate;
    }

    public String getDeliveryDesc() {
        return DeliveryDesc;
    }

    public void setDeliveryDesc(String deliveryDesc) {
        DeliveryDesc = deliveryDesc;
    }

    public Float getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(Float totalMoney) {
        TotalMoney = totalMoney;
    }
}
