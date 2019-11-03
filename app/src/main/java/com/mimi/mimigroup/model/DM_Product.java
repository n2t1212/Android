package com.mimi.mimigroup.model;

public class DM_Product {
    String ProductCode;
    String ProductName;
    String Unit;
    String Specification;
    Boolean isMain;
    Float ConvertKgl;
    Float ConvertBox;

    public DM_Product(){}

    public DM_Product(String productCode,String productName, String unit,String specification,Float convertBox, Boolean misMain){
        this.ProductCode=productCode;
        this.ProductName=productName;
        this.Unit=unit;
        this.Specification=specification;
        this.ConvertBox=convertBox;
        this.isMain=misMain;
    }

    public String getProductCode() {
        return this.ProductCode;
    }

    public void setProductCode(String productCode) {
        this.ProductCode = productCode;
    }

    public String getProductName() {
        return this.ProductName;
    }

    public void setProductName(String productName) {
        this.ProductName = productName;
    }

    public String getUnit() {
        return this.Unit;
    }

    public void setUnit(String unit) {
        this.Unit = unit;
    }

    public String getSpecification() {
        return this.Specification;
    }

    public void setSpecification(String Specification) {
        this.Specification = Specification;
    }

    public void setMain(Boolean main) {
        this.isMain = main;
    }

    public Boolean getMain() {
        return this.isMain;
    }

    public Float getConvertBox() {
        return ConvertBox;
    }
    public void setConvertBox(Float convertBox) {
        ConvertBox = convertBox;
    }
    public Float getConvertKgl() {
        return ConvertKgl;
    }
    public void setConvertKgl(Float convertKgl) {
        ConvertKgl = convertKgl;
    }
}


