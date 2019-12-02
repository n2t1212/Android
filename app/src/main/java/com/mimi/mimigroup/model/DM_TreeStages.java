package com.mimi.mimigroup.model;

public class DM_TreeStages {
    Integer stagesId;
    Integer treeId;
    String stagesCode;
    String stagesName;

    public DM_TreeStages() {
    }

    public DM_TreeStages(Integer stagesId, Integer treeId, String stagesCode, String stagesName) {
        this.stagesId = stagesId;
        this.treeId = treeId;
        this.stagesCode = stagesCode;
        this.stagesName = stagesName;
    }

    public Integer getStagesId() {
        return stagesId;
    }

    public void setStagesId(Integer stagesId) {
        this.stagesId = stagesId;
    }

    public Integer getTreeId() {
        return treeId;
    }

    public void setTreeId(Integer treeId) {
        this.treeId = treeId;
    }

    public String getStagesCode() {
        return stagesCode;
    }

    public void setStagesCode(String stagesCode) {
        this.stagesCode = stagesCode;
    }

    public String getStagesName() {
        return stagesName;
    }

    public void setStagesName(String stagesName) {
        this.stagesName = stagesName;
    }
}
