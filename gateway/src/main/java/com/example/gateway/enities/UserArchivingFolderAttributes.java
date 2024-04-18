package com.example.gateway.enities;

import java.time.LocalDate;
import java.util.ArrayList;

public class UserArchivingFolderAttributes extends ClassificationFolderAttributes {

    Boolean isOpened;
    String ownerID;
    LocalDate progressEndDate;
    LocalDate intermediateEndDate;

    public UserArchivingFolderAttributes() {
    }

    public UserArchivingFolderAttributes(Boolean isOpened, String ownerID, LocalDate progressEndDate, LocalDate intermediateEndDate) {
        this.isOpened = isOpened;
        this.ownerID = ownerID;
        this.progressEndDate = progressEndDate;
        this.intermediateEndDate = intermediateEndDate;
    }

    public UserArchivingFolderAttributes(String code, String arName, String enName, String level, String progressDuration, String intermediateDuration, String finalDetermination, ArrayList<String> groups, String ruleNumber, String parentID, String folderID, Boolean isOpened, String ownerID, LocalDate progressEndDate, LocalDate intermediateEndDate) {
        super(code, arName, enName, level, progressDuration, intermediateDuration, finalDetermination, groups, ruleNumber, parentID, folderID);
        this.isOpened = isOpened;
        this.ownerID = ownerID;
        this.progressEndDate = progressEndDate;
        this.intermediateEndDate = intermediateEndDate;
    }

    public LocalDate getProgressEndDate() {
        return progressEndDate;
    }

    public void setProgressEndDate(LocalDate progressEndDate) {
        this.progressEndDate = progressEndDate;
    }

    public LocalDate getIntermediateEndDate() {
        return intermediateEndDate;
    }

    public void setIntermediateEndDate(LocalDate intermediateEndDate) {
        this.intermediateEndDate = intermediateEndDate;
    }

    public Boolean getOpened() {
        return isOpened;
    }

    public void setOpened(Boolean opened) {
        isOpened = opened;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    @Override
    public String toString() {
        return "UserArchivingFolderAttributes{" +
                "intermediateEndDate=" + intermediateEndDate +
                ", code='" + code + '\'' +
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
                ", progressEndDate=" + progressEndDate +
                ", ownerID='" + ownerID + '\'' +
                ", isOpened=" + isOpened +
                '}';
    }
}
