package com.mimi.mimigroup.model;

public class DM_Customer_Search {
    String Customerid;
    String CustomerCode;
    String CustomerName;
    String ShortName;
    String ProvinceName;
    Double Longititude;
    Double Latitude;


    public DM_Customer_Search(){}
    public DM_Customer_Search(String customerid, String customerCode,String customerName,String shortName,String provinceName,Double longititude,Double latitude){
        this.Customerid=customerid;
        this.CustomerCode=customerCode;
        this.CustomerName=customerName;
        this.ShortName=shortName;
        this.ProvinceName=provinceName;
        this.Longititude=longititude;
        this.Latitude=latitude;
    }

    public String getCustomerid() {
        return Customerid;
    }

    public void setCustomerid(String customerid) {
        Customerid = customerid;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }

    public Double getLongititude() {
        return Longititude;
    }

    public void setLongititude(Double longititude) {
        Longititude = longititude;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }
}

