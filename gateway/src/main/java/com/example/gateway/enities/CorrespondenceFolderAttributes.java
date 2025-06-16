package com.example.gateway.enities;

public class CorrespondenceFolderAttributes {

    String parentID;
    String crsFolderName;

    public CorrespondenceFolderAttributes(String parentID, String crsFolderName) {
        this.parentID = parentID;
        this.crsFolderName = crsFolderName;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getCrsFolderName() {
        return crsFolderName;
    }

    public void setCrsFolderName(String crsFolderName) {
        this.crsFolderName = crsFolderName;
    }
}
