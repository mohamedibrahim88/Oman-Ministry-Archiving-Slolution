package com.example.gateway.DTOs;

public class UserArchivingFolderDTO {

    Boolean isOpened;
    String ownerID;
    String code ;
    String arName;
    String enName ;
    String level;
    String progressDuration;
    String intermediateDuration;
    String finalDetermination;
    String folderID;
    String classificationArName;
    String classificationEnName ;

    public UserArchivingFolderDTO() {
    }

    public UserArchivingFolderDTO(Boolean isOpened, String ownerID, String code, String arName, String enName, String level, String progressDuration, String intermediateDuration, String finalDetermination, String folderID, String classificationArName, String classificationEnName) {
        this.isOpened = isOpened;
        this.ownerID = ownerID;
        this.code = code;
        this.arName = arName;
        this.enName = enName;
        this.level = level;
        this.progressDuration = progressDuration;
        this.intermediateDuration = intermediateDuration;
        this.finalDetermination = finalDetermination;
        this.folderID = folderID;
        this.classificationArName = classificationArName;
        this.classificationEnName = classificationEnName;
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

    public String getFolderID() {
        return folderID;
    }

    public void setFolderID(String folderID) {
        this.folderID = folderID;
    }

    public String getClassificationArName() {
        return classificationArName;
    }

    public void setClassificationArName(String classificationArName) {
        this.classificationArName = classificationArName;
    }

    public String getClassificationEnName() {
        return classificationEnName;
    }

    public void setClassificationEnName(String classificationEnName) {
        this.classificationEnName = classificationEnName;
    }

    @Override
    public String toString() {
        return "UserArchivingFolderDTO{" +
                "isOpened=" + isOpened +
                ", ownerID='" + ownerID + '\'' +
                ", code='" + code + '\'' +
                ", arName='" + arName + '\'' +
                ", enName='" + enName + '\'' +
                ", level='" + level + '\'' +
                ", progressDuration='" + progressDuration + '\'' +
                ", intermediateDuration='" + intermediateDuration + '\'' +
                ", finalDetermination='" + finalDetermination + '\'' +
                ", folderID='" + folderID + '\'' +
                ", classificationArName='" + classificationArName + '\'' +
                ", classificationEnName='" + classificationEnName + '\'' +
                '}';
    }
}
