package com.mimi.mimigroup.model;

public class DM_Tree_Disease {
    int DiseaseID;
    int TreeID;
    String DiseaseCode;
    String DiseaseName;
    String TreeName;

    public DM_Tree_Disease() {
    }

    public DM_Tree_Disease(int diseaseID, int treeID,String diseaseCode, String diseaseName) {
        this.TreeID = treeID;
        this.DiseaseID = diseaseID;
        this.DiseaseName = diseaseName;
        this.DiseaseCode=diseaseCode;
    }

    public int getTreeID() {
        return TreeID;
    }

    public void setTreeID(int treeID) {
        TreeID = treeID;
    }

    public int getDiseaseID() {
        return DiseaseID;
    }

    public void setDiseaseID(int diseaseID) {
        DiseaseID = diseaseID;
    }

    public String getDiseaseName() {
        return DiseaseName;
    }

    public String getDiseaseCode() {
        return DiseaseCode;
    }

    public void setDiseaseCode(String diseaseCode) {
        DiseaseCode = diseaseCode;
    }

    public void setDiseaseName(String diseaseName) {
        DiseaseName = diseaseName;
    }

    public String getTreeName() {
        return TreeName;
    }

    public void setTreeName(String treeName) {
        TreeName = treeName;
    }
}
