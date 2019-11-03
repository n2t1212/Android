package com.mimi.mimigroup.model;

public class DM_District {
    int Districtid;
    int Provinceid;
    String DistrictCode;
    String District;
    String Province;

    public DM_District() {
    }

    public DM_District(int districtid, int provinceid, String districtCode, String district) {
        this.Districtid = districtid;
        this.Provinceid = provinceid;
        this.DistrictCode = districtCode;
        this.District = district;
    }

    public Integer getDistrictid() {
        return Districtid;
    }
    public void setDistrictid(int districtid) {
        Districtid = districtid;
    }

    public Integer getProvinceid() {
        return Provinceid;
    }
    public void setProvinceid(int provinceid) {
        Provinceid = provinceid;
    }

    public String getDistrictCode() {
        return DistrictCode;
    }
    public void setDistrictCode(String districtCode) {
        DistrictCode = districtCode;
    }

    public String getDistrict() {
        return District;
    }
    public void setDistrict(String district) {
        District = district;
    }

    public String getProvince() {
        return this.Province;
    }
    public void setProvince(String province) {
      this.Province=province;
    }

}
