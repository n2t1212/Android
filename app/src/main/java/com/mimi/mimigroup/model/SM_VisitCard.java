package com.mimi.mimigroup.model;

public class SM_VisitCard {
    String VisitCardID;
    String VisitDay;
    String VisitType;
    String CustomerID;
    String CustomerName;
    String VisitTime;
    Double Longitude;
    Double Latitude;
    String LocationAddress;
    String VisitNotes;
    Boolean IsSync;
    String SyncDay;

    public SM_VisitCard() {
    }

    public SM_VisitCard(String visitCardID,String visitType,String visitDay,String customerID,String visitTime,Double longitude,Double latitude,String locationAddress,boolean isSync,String syncDay) {
        this.VisitCardID=visitCardID;
        this.VisitDay=visitDay;
        this.VisitType=visitType;
        this.CustomerID=customerID;
        this.VisitTime=visitTime;
        this.Longitude=longitude;
        this.Latitude=latitude;
        this.LocationAddress=locationAddress;
        this.SyncDay=syncDay;
        this.IsSync=isSync;
    }

    public String getVisitCardID() {
        return VisitCardID;
    }

    public void setVisitCardID(String visitCardID) {
        this.VisitCardID = visitCardID;
    }

    public String getVisitType() {
        return VisitType;
    }

    public void setVisitType(String visitType) {
        this.VisitType = visitType;
    }


    public String getVisitDay() {
        return VisitDay;
    }

    public void setVisitDay(String visitDay) {
        this.VisitDay = visitDay;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        this.CustomerID = customerID;
    }

    public String getVisitTime() {
        return VisitTime;
    }
    public void setVisitTime(String visitTime) {
        this.VisitTime = visitTime;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        this.CustomerName = customerName;
    }

    public String getLocationAddress() {
        return LocationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.LocationAddress = locationAddress;
    }

    public String getSyncDay() {
        return SyncDay;
    }

    public void setSyncDay(String syncDay) {
        this.SyncDay = syncDay;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        this.Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        this.Longitude = longitude;
    }

    public Boolean getSync() {
        return IsSync;
    }

    public void setSync(Boolean sync) {
        this.IsSync = sync;
    }

    public String getVisitNotes() {
        return VisitNotes;
    }
    public void setVisitNotes(String visitNotes) {
        VisitNotes = visitNotes;
    }
}
