package com.mimi.mimigroup.model;

public class KS_Search {
    int Seqno;
    String CustomerCode;
    String CustomerName;
    String EmployeeName;
    String AddressScan;
    Float Longitude;
    Float Latitude;
    String Province;
    String Ranked;
    String TimeScan;
    String ProductCode;
    String ProductName;
    String Unit;
    String Specification;
    Integer Quantity;
    String Notes;
    String EmployeeTel;
    Boolean isFish;

    public KS_Search() {
    }

    public KS_Search(int seqno, String customerCode, String customerName, String employeeName, String addressScan, String timeScan,String productCode,String productName,String unit,String specification, String notes, boolean isFish) {
        this.Seqno = seqno;
        this.CustomerCode = customerCode;
        this.CustomerName = customerName;
        this.EmployeeName = employeeName;
        this.AddressScan = addressScan;
        this.TimeScan = timeScan;
        this.Notes = notes;
        this.ProductCode=productCode;
        this.ProductName=productName;
        this.Unit=unit;
        this.Specification=specification;
        this.isFish = isFish;
    }

    public int getSeqno() {
        return Seqno;
    }

    public void setSeqno(int seqno) {
        Seqno = seqno;
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

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }


    public String getEmployeeName() {
        return EmployeeName;
    }
    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getAddressScan() {
        return AddressScan;
    }

    public void setAddressScan(String addressScan) {
        AddressScan = addressScan;
    }

    public String getTimeScan() {
        return TimeScan;
    }

    public String getRanked() {
        return Ranked;
    }

    public void setRanked(String ranked) {
        Ranked = ranked;
    }

    public void setTimeScan(String timeScan) {
        TimeScan = timeScan;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public Boolean getFish() {
        return isFish;
    }

    public void setFish(Boolean fish) {
        isFish = fish;
    }

    public Float getLongitude() {
        return Longitude;
    }

    public void setLongitude(Float longitude) {
        Longitude = longitude;
    }

    public Float getLatitude() {
        return Latitude;
    }

    public void setLatitude(Float latitude) {
        Latitude = latitude;
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

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }

    public String getEmployeeTel() {
        return EmployeeTel;
    }

    public void setEmployeeTel(String employeeTel) {
        EmployeeTel = employeeTel;
    }
}

