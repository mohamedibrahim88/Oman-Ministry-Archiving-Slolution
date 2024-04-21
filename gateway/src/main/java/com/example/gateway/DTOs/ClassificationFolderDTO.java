package com.example.gateway.DTOs;

public class ClassificationFolderDTO {

    String code ;
    String arName;
    String enName ;
    String progressDuration;
    String intermediateDuration;
    String finalDetermination;
    String folderID;

    public ClassificationFolderDTO() {
    }

    public ClassificationFolderDTO(String code, String arName, String enName, String progressDuration, String intermediateDuration, String finalDetermination, String folderID) {
        this.code = code;
        this.arName = arName;
        this.enName = enName;
        this.progressDuration = progressDuration;
        this.intermediateDuration = intermediateDuration;
        this.finalDetermination = finalDetermination;
        this.folderID = folderID;
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

    @Override
    public String toString() {
        return "ClassificationFolderDTO{" +
                "code='" + code + '\'' +
                ", arName='" + arName + '\'' +
                ", enName='" + enName + '\'' +
                ", progressDuration='" + progressDuration + '\'' +
                ", intermediateDuration='" + intermediateDuration + '\'' +
                ", finalDetermination='" + finalDetermination + '\'' +
                ", folderID='" + folderID + '\'' +
                '}';
    }
}
