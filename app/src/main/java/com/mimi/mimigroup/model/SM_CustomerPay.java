package com.mimi.mimigroup.model;

public class SM_CustomerPay {
    String PayID;
    String PayCode;
    String PayDate;
    String PayName;
    String CustomerID;
    String CustomerCode;
    String CustomerName;
    String CustomerAddress;

    Double PayMoney;
    String PayPic;
    Double Longitude;
    Double Latitude;
    String LocationAddress;
    Boolean IsPost;
    String PostDay;
    String PayNotes;
    Integer PayStatus;
    String PayStatusDesc;
    String PayPicBase;
    public SM_CustomerPay() { }
    public SM_CustomerPay(String payID,String payCode,String payDate,String payName,String customerID,double payMoney,String payPic,
                          String payNotes){
       this.PayID=payID;
       this.PayCode=payCode;
       this.PayName=payName;
       this.CustomerID=customerID;
       this.PayMoney=payMoney;
       this.PayNotes=payNotes;
       this.PayPic=payPic;
    }
    public String getPayID() {
        return PayID;
    }
    public void setPayID(String payID) {
        PayID = payID;
    }

    public String getPayCode() {
        return PayCode;
    }
    public void setPayCode(String payCode) {
        PayCode = payCode;
    }

    public String getPayName() {
        return PayName;
    }
    public void setPayName(String payName) {
        PayName = payName;
    }

    public Double getPayMoney() {
        return PayMoney;
    }
    public void setPayMoney(Double payMoney) {
        PayMoney = payMoney;
    }

    public String getPayDate() {
        return PayDate;
    }

    public void setPayDate(String payDate) {
        PayDate = payDate;
    }

    public String getPayPic() {
        return PayPic;
    }
    public void setPayPic(String payPic) {
        PayPic = payPic;
    }

    public String getPayPicBase() {
        return PayPicBase;
    }
    public void setPayPicBase(String payPicBase) {
        PayPicBase = payPicBase;
    }

    public String getPayNotes() {
        return PayNotes;
    }
    public void setPayNotes(String payNotes) {
        PayNotes = payNotes;
    }

    public Integer getPayStatus() {
        return PayStatus;
    }
    public void setPayStatus(Integer payStatus) {
        PayStatus = payStatus;
    }

    public String getPayStatusDesc() {
        return PayStatusDesc;
    }

    public void setPayStatusDesc(String payStatusDesc) {
        PayStatusDesc = payStatusDesc;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }
    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public String getLocationAddress() {
        return LocationAddress;
    }
    public void setLocationAddress(String locationAddress) {
        LocationAddress = locationAddress;
    }

    public Boolean getPost() {
        return IsPost;
    }
    public void setPost(Boolean post) {
        IsPost = post;
    }

    public String getPostDay() {
        return PostDay;
    }
    public void setPostDay(String postDay) {
        PostDay = postDay;
    }



    public String getCustomerID() {
        return CustomerID;
    }
    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getCustomerName() {
        return CustomerName;
    }
    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerAddress() {
        return CustomerAddress;
    }
    public void setCustomerAddress(String customerAddress) {
        CustomerAddress = customerAddress;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }
    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

}
