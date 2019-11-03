package com.mimi.mimigroup.model;

public class SM_OrderTotalSales {
    String FDay;
    String TDay;
    Double TotalMoney;
    Double TotalPoint;

    Integer Seqno;
    String OrderCode;
    String OrderDate;
    Double OrderMoney;
    String OrderStatus;
    String OrderNotes;

    public SM_OrderTotalSales() {
    }

    public SM_OrderTotalSales(String fday,String tday,double totalMoney,double totalPoint,String orderCode,String orderDate,Double orderMoney,String orderStatus,String orderNotes) {
         this.FDay=fday;
         this.TDay=tday;
         this.TotalMoney=totalMoney;
         this.TotalPoint=totalPoint;
         this.OrderCode=orderCode;
         this.OrderDate=orderDate;
         this.OrderMoney=orderMoney;
         this.OrderStatus=orderStatus;
         this.OrderNotes=orderNotes;
    }


    public String getFDay() {
        return FDay;
    }

    public void setFDay(String FDay) {
        this.FDay = FDay;
    }

    public String getTDay() {
        return TDay;
    }
    public void setTDay(String TDay) {
        this.TDay = TDay;
    }

    public Double getTotalMoney() {
        return TotalMoney;
    }
    public void setTotalMoney(Double totalMoney) {
        TotalMoney = totalMoney;
    }

    public Double getTotalPoint() {
        return TotalPoint;
    }
    public void setTotalPoint(Double totalPoint) {
        TotalPoint = totalPoint;
    }


    public Integer getSeqno() {
        return Seqno;
    }

    public void setSeqno(Integer seqno) {
        Seqno = seqno;
    }

    public String getOrderCode() {
        return OrderCode;
    }
    public void setOrderCode(String orderCode) {
        OrderCode = orderCode;
    }

    public Double getOrderMoney() {
        return OrderMoney;
    }
    public void setOrderMoney(Double orderMoney) {
        OrderMoney = orderMoney;
    }

    public String getOrderDate() {
        return OrderDate;
    }
    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }
    public void setOrderNotes(String orderNotes) {
        OrderNotes = orderNotes;
    }

    public String getOrderNotes() {
        return OrderNotes;
    }

}
