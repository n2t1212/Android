package com.mimi.mimigroup.model;

public class DM_Customer_Distance implements Comparable<DM_Customer_Distance>{
    String Customerid;
    String CustomerName;
    Double Longititude;
    Double Latitude;
    Float Distance;
    Integer SeqOrder;

    public DM_Customer_Distance(){}

    public DM_Customer_Distance(String Customerid,String CustomerName,Double Longitude,Double Latitude,Integer SeqOrder){
        this.Customerid=Customerid;
        this.CustomerName=CustomerName;
        this.Longititude=Longitude;
        this.Latitude=Latitude;
        this.SeqOrder=SeqOrder;
    }

    public String getCustomerid() {
        return this.Customerid;
    }

    public void setCustomerid(String customerid) {
        this.Customerid = customerid;
    }

    public String getCustomerName() {
        return this.CustomerName;
    }

    public void setCustomerName(String customerName) {
        this.CustomerName = customerName;
    }

    public Double getLongititude() {
        return this.Longititude;
    }

    public void setLongititude(Double longititude) {
        this.Longititude = longititude;
    }

    public Double getLatitude() {
        return this.Latitude;
    }
    public void setLatitude(Double latitude) {
        this.Latitude = latitude;
    }

    public Float getDistance() {
        return this.Distance;
    }
    public void setDistance(Float distance) {
        this.Distance = distance;
    }

    public Integer getSeqOrder() {
        return this.SeqOrder;
    }

    public void setSeqOrder(Integer seqOrder) {
        this.SeqOrder = seqOrder;
    }

    @Override
    public int compareTo(DM_Customer_Distance oCus){
        if(getDistance()==null || oCus.getDistance()==null){
            return  0;
        }
        return getDistance().compareTo(oCus.getDistance());
    }
}


