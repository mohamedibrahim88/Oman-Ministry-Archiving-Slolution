package com.example.gateway.DTOs;

public class CorrespondenceFolderDTO {

    String folderID;
    String folderName;

    public CorrespondenceFolderDTO() {
    }

    public CorrespondenceFolderDTO(String folderID, String folderName) {
        this.folderID = folderID;
        this.folderName = folderName;
    }

    public String getFolderID() {
        return folderID;
    }

    public void setFolderID(String folderID) {
        this.folderID = folderID;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
