package com.mimi.mimigroup.model;

public class DM_Employee {
    private String Employeeid;
    private String EmployeeCode;
    private String EmployeeName;
    private String Notes;

    public DM_Employee(){}

    public DM_Employee(String employeeid,String employeeCode,String employeeName,String notes){
        this.Employeeid=employeeid;
        this.EmployeeCode=employeeCode;
        this.EmployeeName=employeeName;
        this.Notes=notes;
    }

    public String getEmployeeid() {
        return Employeeid;
    }

    public void setEmployeeid(String employeeid) {
        this.Employeeid = employeeid;
    }

    public String getEmployeeCode() {
        return EmployeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        EmployeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }
}
