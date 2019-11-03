package com.mimi.mimigroup.model;

public class DM_Ward {
    int Wardid;
    int Districtid;
    String Ward;
    String District;

    public DM_Ward() {
    }

    public DM_Ward(int wardid, int districtid, String ward) {
        this.Wardid = wardid;
        this.Districtid= districtid;
        this.Ward = ward;
    }

    public int getWardid() {
        return this.Wardid;
    }
    public void setWardid(int wardid) {
        this.Wardid = wardid;
    }

    public int getDistrictid() {
        return this.Districtid;
    }
    public void setDistrictid(int districtid) {
        this.Districtid = districtid;
    }

    public String getWard() {
        return this.Ward;
    }

    public void setWard(String ward) {
        this.Ward = ward;
    }

    public String getDistrict() {
        return this.District;
    }

    public void setDistrict(String District) {
        this.District = District;
    }
}
