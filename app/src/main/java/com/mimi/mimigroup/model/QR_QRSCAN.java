package com.mimi.mimigroup.model;

public class QR_QRSCAN {
    String Qrscanid;
    String Customerid;
    String CommandNo;
    String Qrid;
    String ProductCode;
    String ProductName;
    String Unit;
    String Specification;
    Integer ScanNo;
    String ScanDay;
    Double Longitude;
    Double Latitude;
    String LocationAddress;
    String Imei;
    String ImeiSim;
    String SyncDay;
    Boolean IsSync;
    String CustomerName;
    String ScanSupportID;
    String ScanSupportDesc;
    String ScanType;

    public QR_QRSCAN() {
    }

    public QR_QRSCAN(String qrscanid,String customerid,String commandNo,String qrid,String productCode,String productName,String unit,
                     String specification,Integer scanNo,String scanDay,Double longitude,Double latitude,String locationAddress,String scanSupportID,String imei,
                     String imeiSim,String syncDay,Boolean isSync,String scanType){
       this.Qrscanid=qrscanid;
       this.Customerid=customerid;
       this.CommandNo=commandNo;
       this.Qrid=qrid;
       this.ProductCode=productCode;
       this.ProductName=productName;
       this.Unit=unit;
       this.Specification=specification;
       this.ScanNo=scanNo;
       this.ScanDay=scanDay;
       this.Longitude=longitude;
       this.Latitude=latitude;
       this.LocationAddress=locationAddress;
       this.ScanSupportID=scanSupportID;
       this.Imei=imei;
       this.ImeiSim=imeiSim;
       this.SyncDay=syncDay;
       this.IsSync=isSync;
       this.ScanType=scanType;
    }


    public String getQrscanid() {
        return Qrscanid;
    }
    public void setQrscanid(String qrscanid) {
        Qrscanid = qrscanid;
    }

    public String getQrid() {
        return Qrid;
    }

    public void setQrid(String qrid) {
        Qrid = qrid;
    }

    public String getCustomerid() {
        return Customerid;
    }
    public void setCustomerid(String customerid) {
        Customerid = customerid;
    }

    public String getCommandNo() {
        return CommandNo;
    }

    public void setCommandNo(String commandNo) {
        CommandNo = commandNo;
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

    public String getSpecification() {
        return Specification;
    }
    public void setSpecification(String specification) {
        Specification = specification;
    }

    public Integer getScanNo() {
        return ScanNo;
    }
    public void setScanNo(Integer scanNo) {
        ScanNo = scanNo;
    }

    public String getScanDay() {
        return ScanDay;
    }
    public void setScanDay(String scanDay) {
        ScanDay = scanDay;
    }

    public Double getLongitude() {
        return Longitude;
    }
    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getLatitude() {
        return Latitude;
    }
    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public String getLocationAddress() {
        return LocationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        LocationAddress = locationAddress;
    }

    public String getScanSupportID() {
        return ScanSupportID;
    }

    public void setScanSupportID(String scanSupportID) {
        this.ScanSupportID = scanSupportID;
    }

    public String getScanSupportDesc() {
        return ScanSupportDesc;
    }

    public void setScanSupportDesc(String scanSupportDesc) {
        this.ScanSupportDesc = scanSupportDesc;
    }

    public String getImei() {
        return Imei;
    }
    public void setImei(String imei) {
        Imei = imei;
    }

    public String getImeiSim() {
        return ImeiSim;
    }
    public void setImeiSim(String imeiSim) {
        ImeiSim = imeiSim;
    }

    public String getSyncDay() {
        return SyncDay;
    }
    public void setSyncDay(String syncDay) {
        SyncDay = syncDay;
    }

    public Boolean getSync() {
        return this.IsSync;
    }
    public void setSync(Boolean sync) {
        this.IsSync = sync;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getScanType() {
        return ScanType;
    }

    public void setScanType(String scanType) {
        this.ScanType = scanType;
    }
}
