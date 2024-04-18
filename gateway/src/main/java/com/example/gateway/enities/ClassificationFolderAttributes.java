package com.example.gateway.enities;

import java.util.ArrayList;

public class ClassificationFolderAttributes {
    String code ;
    String arName;
    String enName ;
    String level;
    String progressDuration;
    String intermediateDuration;
    String finalDetermination;
    ArrayList<String> groups;
    String ruleNumber;
    String parentID;
    String folderID;

    public ClassificationFolderAttributes() {
    }

    public ClassificationFolderAttributes(String code, String arName, String enName, String level, String progressDuration, String intermediateDuration, String finalDetermination, ArrayList<String> groups, String ruleNumber, String parentID, String folderID) {
        this.code = code;
        this.arName = arName;
        this.enName = enName;
        this.level = level;
        this.progressDuration = progressDuration;
        this.intermediateDuration = intermediateDuration;
        this.finalDetermination = finalDetermination;
        this.groups = groups;
        this.ruleNumber = ruleNumber;
        this.parentID = parentID;
        this.folderID = folderID;
    }

    public String getRuleNumber() {
        return ruleNumber;
    }

    public void setRuleNumber(String ruleNumber) {
        this.ruleNumber = ruleNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getArName() {
        return arName;
    }

    public void setArName(String arName) {
        this.arName = arName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getProgressDuration() {
        return progressDuration;
    }

    public void setProgressDuration(String progressDuration) {
        this.progressDuration = progressDuration;
    }

    public String getIntermediateDuration() {
        return intermediateDuration;
    }

    public void setIntermediateDuration(String intermediateDuration) {
        this.intermediateDuration = intermediateDuration;
    }

    public String getFinalDetermination() {
        return finalDetermination;
    }

    public void setFinalDetermination(String finalDetermination) {
        this.finalDetermination = finalDetermination;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getFolderID() {
        return folderID;
    }

    public void setFolderID(String folderID) {
        this.folderID = folderID;
    }

    @Override
    public String toString() {
        return "FolderAttributes{" +
                "code='" + code + '\'' +
                ", arName='" + arName + '\'' +
                ", enName='" + enName + '\'' +
                ", level='" + level + '\'' +
                ", progressDuration='" + progressDuration + '\'' +
                ", intermediateDuration='" + intermediateDuration + '\'' +
                ", finalDetermination='" + finalDetermination + '\'' +
                ", groups=" + groups +
                ", ruleNumber='" + ruleNumber + '\'' +
                ", parentID='" + parentID + '\'' +
                ", folderID='" + folderID + '\'' +
                '}';
    }
}
