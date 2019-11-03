package com.mimi.mimigroup.model;

public class DM_Province {
    private int Provinceid;
    private String ZoneCode;
    private String Province;
    private String ProvinceCode;

    public DM_Province(){}

    public DM_Province(Integer Provinceid,String ZoneCode,String Province,String ProvinceCode){
        this.Provinceid=Provinceid;
        this.ZoneCode=ZoneCode;
        this.Province=Province;
        this.ProvinceCode=ProvinceCode;
    }

    public Integer getProvinceid(){return this.Provinceid;}
    public void setProvinceid(Integer Provinceid){this.Provinceid=Provinceid;}

    public String getProvince(){return this.Province;}
    public void setProvince(String Province){this.Province=Province;}

    public String getZoneCode(){return this.ZoneCode;}
    public void setZoneCode(String ZoneCode){this.ZoneCode=ZoneCode;}

    public String getProvinceCode(){return this.ProvinceCode;}
    public void setProvinceCode(String ProvinceCode){this.ProvinceCode=ProvinceCode;}

}
