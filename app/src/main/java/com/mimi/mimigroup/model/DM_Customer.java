package com.mimi.mimigroup.model;

public class DM_Customer {
    String Customerid;
    String Employeeid;
    String CustomerCode;
    String CustomerName;
    String ShortName;
    String Represent;
    int Provinceid;
    int Districtid;
    int Wardid;
    String Street;
    String Address;
    String Tax;
    int isLevel;
    String Tel;
    String Fax;
    String Email;
    String VisitSchedule;
    String VisitDay;
    String Ranked;
    String ExtCustomer;
    double Longitude;
    double Latitude;
    String LocationAddress;

    double LongitudeTemp;
    double LatitudeTemp;
    String LocationAddressTemp;
    double Scope;
    int isStatus;
    boolean isLocate;
    int Edit;
    String Notes;
    float Distance;

    String ProvinceName;
    String DistrictName;
    String WardName;
    String StatusDesc;

    public  DM_Customer(){}

    public DM_Customer(String Customerid,String Employeeid,String CustomerCode,String CustomerName,String ShortName, String Represent,
                       int Provinceid, int Districtid, int Wardid,String Street, String Address,String Tax,int isLevel,String Tel, String Fax,
                       String Email,String VisitSchedule,String VisitDay,String Ranked,String ExtCustomer,Double Longitude,Double Latitude,
                       Double LongitudeTemp,Double LatitudeTemp,Double Scope,String locationAddress,String locationAddressTemp,int isStatus,Boolean isLocate,int Edit,String Notes,Float Distance)
    {
        this.Customerid=Customerid;
        this.Employeeid=Employeeid;
        this.CustomerCode=CustomerCode;
        this.CustomerName=CustomerName;
        this.ShortName=ShortName;
        this.Represent=Represent;
        this.Provinceid=Provinceid;
        this.Districtid=Districtid;
        this.Wardid=Wardid;
        this.Street=Street;
        this.Address=Address;
        this.Tax=Tax ;
        this.isLevel=isLevel;
        this.Tel =Tel;
        this.Fax=Fax;
        this.Email=Email;
        this.VisitSchedule=VisitSchedule;
        this.VisitDay=VisitDay;
        this.Ranked=Ranked;
        this.ExtCustomer=ExtCustomer;
        this.Longitude=Longitude;
        this.Latitude=Latitude;
        this.LongitudeTemp=LongitudeTemp;
        this.LatitudeTemp=LatitudeTemp;
        this.Scope=Scope;
        this.LocationAddress=locationAddress;
        this.LocationAddressTemp=locationAddressTemp;
        this.isStatus=isStatus;
        this.isLocate=isLocate;
        this.Edit=Edit;
        this.Notes=Notes;
        this.Distance=Distance;

        this.ProvinceName="";
        this.DistrictName="";
        this.WardName="";
        this.StatusDesc="";
    }

    public String getCustomerid() {
        return Customerid;
    }
    public void setCustomerid(String customerid) {
        this.Customerid = customerid;
    }

    public String getEmployeeid() {
        return Employeeid;
    }
    public void setEmployeeid(String employeeid) {
        this.Employeeid = employeeid;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.CustomerCode = customerCode;
    }

    public String getCustomerName() {
        return CustomerName;
    }
    public void setCustomerName(String customerName) {
        this.CustomerName = customerName;
    }

    public String getShortName() {
        return ShortName;
    }
    public void setShortName(String shortName) {
        this.ShortName = shortName;
    }

    public String getRepresent() {
        return Represent;
    }

    public void setRepresent(String represent) {
        this.Represent = represent;
    }

    public Integer getProvinceid() {
        return Provinceid;
    }

    public void setProvinceid(int provinceid) {
        this.Provinceid = provinceid;
    }

    public Integer getDistrictid() {
        return Districtid;
    }

    public void setDistrictid(int districtid) {
        this.Districtid = districtid;
    }

    public Integer getWardid() {
        return Wardid;
    }

    public void setWardid(int wardid) {
        this.Wardid = wardid;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        this.Street = street;
    }
    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }
    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        this.Tel = tel;
    }


    public void setExtCustomer(String extCustomer) {
        this.ExtCustomer = extCustomer;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFax() {
        return Fax;
    }
    public void setFax(String fax) {
        Fax = fax;
    }

    public Integer getIsLevel() {
        return isLevel;
    }

    public void setIsLevel(int isLevel) {
        this.isLevel = isLevel;
    }

    public String getTax() {
        return Tax;
    }
    public void setTax(String tax) {
        this.Tax = tax;
    }

    public String getVisitSchedule() {
        return VisitSchedule;
    }

    public void setVisitSchedule(String visitSchedule) {
        this.VisitSchedule = visitSchedule;
    }

    public String getVisitDay() {
        return VisitDay;
    }

    public void setVisitDay(String visitDay) {
        this.VisitDay = visitDay;
    }

    public String getRanked() {
        return Ranked;
    }
    public void setRanked(String ranked) {
        this.Ranked = ranked;
    }

    public double getLongitude() {
        return Longitude;
    }
    public void setLongitude(double longitude) {
        this.Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }
    public void setLatitude(double latitude) {
        this.Latitude = latitude;
    }

    public Double getLongitudeTemp() {
        return LongitudeTemp;
    }
    public void setLongitudeTemp(double longitudeTemp) {
        this.LongitudeTemp = longitudeTemp;
    }

    public Double getLatitudeTemp() {
        return LatitudeTemp;
    }

    public void setLatitudeTemp(double latitudeTemp) {
        this.LatitudeTemp = latitudeTemp;
    }

    public String getLocationAddress() {
        return this.LocationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.LocationAddress = locationAddress;
    }

    public String getLocationAddressTemp() {
        return this.LocationAddressTemp;
    }

    public void setLocationAddressTemp(String locationAddressTemp) {
        this.LocationAddressTemp = locationAddressTemp;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        this.Notes = notes;
    }



    public Integer getEdit() {
        return Edit;
    }

    public void setEdit(int edit) {
        this.Edit = edit;
    }

    public float getDistance() {
        return Distance;
    }

    public void setDistance(float distance) {
        this.Distance = distance;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        this.ProvinceName = provinceName;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        this.DistrictName = districtName;
    }

    public String getWardName() {
        return WardName;
    }

    public void setWardName(String wardName) {
        this.WardName = wardName;
    }

    public String getStatusDesc() {
        return StatusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.StatusDesc = statusDesc;
    }
}
