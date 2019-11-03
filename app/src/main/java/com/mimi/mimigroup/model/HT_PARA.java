package com.mimi.mimigroup.model;

public class HT_PARA {
    String ParaCode;
    String ParaValue;

    public HT_PARA(){}

    public HT_PARA(String ParaCode,String ParaValue){
        this.ParaCode=ParaCode;
        this.ParaValue=ParaValue;
    }

    public String getParaCode(){return this.ParaCode;}
    public void setParaCode(String ParaCode){this.ParaCode=ParaCode;}

    public String getParaValue(){return this.ParaValue;}
    public void setParaValue(String ParaValue){this.ParaValue=ParaValue;}

}
