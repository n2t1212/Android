package com.mimi.mimigroup.model;

public class DM_Tree {
    int TreeID;
    String TreeCode;
    String TreeGroupCode;
    String TreeName;

    public DM_Tree() {
    }

    public DM_Tree(int treeID, String treeCode, String treeName) {
        this.TreeID =treeID;
        this.TreeCode =treeCode;
        this.TreeName =treeName;
    }

    public int getTreeID() {
        return TreeID;
    }

    public void setTreeID(int treeID) {
        TreeID = treeID;
    }

    public String getTreeCode() {
        return TreeCode;
    }

    public void setTreeCode(String treeCode) {
        TreeCode = treeCode;
    }

    public String getTreeGroupCode() {
        return TreeGroupCode;
    }

    public void setTreeGroupCode(String treeGroupCode) {
        TreeGroupCode = treeGroupCode;
    }

    public String getTreeName() {
        return TreeName;
    }

    public void setTreeName(String treeName) {
        TreeName = treeName;
    }
}
