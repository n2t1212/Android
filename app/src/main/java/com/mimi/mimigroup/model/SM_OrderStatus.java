package com.mimi.mimigroup.model;

public class SM_OrderStatus {
    String OrderID;
    String OrderCode;
    Integer OrderStatus;
    Double TotalMoney;
    String OrderStatusDesc;

    public SM_OrderStatus() {
    }

    public SM_OrderStatus(String orderId, String orderCode, Double totalMoney, Integer orderStatus,String orderStatusDesc ) {
        this.OrderID=orderId;
        this.OrderCode=orderCode;
        this.TotalMoney=totalMoney;
        this.OrderStatus=orderStatus;
        this.OrderStatusDesc=orderStatusDesc;
    }


    public String getOrderID() {
        return OrderID;
    }
    public void setOrderID(String orderId) {
        OrderID = orderId;
    }

    public String getOrderCode() {
        return OrderCode;
    }
    public void setOrderCode(String orderCode) {
        OrderCode = orderCode;
    }

    public Double getTotalMoney() {
        return TotalMoney;
    }
    public void setTotalMoney(Double totalMoney) {
        TotalMoney = totalMoney;
    }

    public void setOrderStatus(Integer orderStatus) {
        OrderStatus = orderStatus;
    }
    public Integer getOrderStatus() {
        return OrderStatus;
    }

    public String getOrderStatusDesc() {
        return OrderStatusDesc;
    }

    public void setOrderStatusDesc(String orderStatusDesc) {
        OrderStatusDesc = orderStatusDesc;
    }
}
