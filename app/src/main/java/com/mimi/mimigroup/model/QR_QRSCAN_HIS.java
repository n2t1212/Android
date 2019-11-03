package com.mimi.mimigroup.model;

public class QR_QRSCAN_HIS {
    String Qrid;
    String ProductCode;
    String ProductName;
    String Customerid;
    String Employee;
    Integer ScanNo;
    String ScanDay;
    String LocationAddress;
    String CustomerName;


    public QR_QRSCAN_HIS(){}

    public QR_QRSCAN_HIS(String qrid, String customerid, String employee, String scanday,Integer scanNo, String locationAddress, String productCode, String productName, String customerName){
        this.Qrid=qrid;
        this.Customerid=customerid;
        this.Employee=employee;
        this.ScanDay=scanday;
        this.ScanNo=scanNo;
        this.LocationAddress=locationAddress;
        this.ProductCode=productCode;
        this.ProductName=productName;
        this.CustomerName=customerName;
    }

    public String getQrid() {
        return Qrid;
    }

    public void setQrid(String qrid) {
        this.Qrid = qrid;
    }

    public String getCustomerid() {
        return Customerid;
    }

    public void setCustomerid(String customerid) {
        this.Customerid = customerid;
    }

    public String getScanDay() {
        return ScanDay;
    }
    public void setScanDay(String scanday) {
        this.ScanDay = scanday;
    }

    public String getLocationAddress() {
        return this.LocationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.LocationAddress = locationAddress;
    }

    public Integer getScanNo() {
        return ScanNo;
    }

    public void setScanNo(Integer scanNo) {
        this.ScanNo = scanNo;
    }

    public String getEmployee() {
        return Employee;
    }

    public void setEmployee(String employee) {
        this.Employee = employee;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        this.ProductCode = productCode;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        this.ProductName = productName;
    }

    public String getCustomerName() {
        return this.CustomerName;
    }

    public void setCustomerName(String customerName) {
        this.CustomerName= customerName;
    }
}
