package com.example.gateway.service;

import com.example.gateway.DTOs.ClassificationFolderDTO;
import com.example.gateway.DTOs.UserArchivingFolderDTO;
import com.example.gateway.client.FileNet;
import com.example.gateway.enities.UserArchivingFolderAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class FileService {

    @Autowired
    FileNet fileNet;
    public ArrayList<ClassificationFolderDTO> getClassificationsFolderByOwnerID(String organization, String filterStr) {
        return fileNet.getClassificationsFolderByOwnerID(organization, filterStr);
    }

    public UserArchivingFolderDTO createArchive(UserArchivingFolderAttributes folderAttributes){
            return fileNet.createArchive(folderAttributes);
    }
    public void createCorrespondence(){

    }
    public void createDocument(){

    }
    public ArrayList<UserArchivingFolderDTO> getUserFoldersByOwnerID(String ownerID, String filterStr){
        return fileNet.getUserFoldersByOwnerID(ownerID, filterStr);
    }
    public ArrayList<UserArchivingFolderDTO> getUserFoldersByStatus(String ownerID, boolean isOpened){
        return fileNet.getUserFoldersByStatus(ownerID, isOpened);
    }
    public void getCorrespondenceByFileID(){

    }
    public void updateFileStatus(String folderID){
        fileNet.updateFolderStatus(folderID);
    }
    public void deleteFileById(String folderID){
        fileNet.deleteFolderByID(folderID);
    }

    public void GetNumbersOfCorrespondenceByStatus(){

    }



}
