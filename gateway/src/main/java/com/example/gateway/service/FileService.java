package com.example.gateway.service;

import com.example.gateway.DTOs.UserArchivingFolderDTO;
import com.example.gateway.client.FileNet;
import com.example.gateway.enities.ClassificationFolderAttributes;
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
    public ArrayList<ClassificationFolderAttributes> getLeafFoldersByOwnerID(String ownerId){
        return fileNet.getLeafFoldersByOwnerID(ownerId);
    }

    public UserArchivingFolderDTO createArchive(UserArchivingFolderAttributes folderAttributes){
            return fileNet.createArchive(folderAttributes);
    }
    public void createCorrespondence(){

    }
    public void createDocument(){

    }
    public ArrayList<UserArchivingFolderAttributes> GetFilesByOwnerId(String ownerID){
        return fileNet.getFilesByOwnerID(ownerID);
    }
    public void getCorrespondenceByFileID(){

    }
    public void updateFileStatus(String folderID, boolean isOpend){
        fileNet.updateFolderStatus(folderID, isOpend);
    }
    public void deleteFileById(String folderID){
        fileNet.deleteFolderByID(folderID);
    }

    public void GetNumbersOfCorrespondenceByStatus(){

    }
    public ArrayList<UserArchivingFolderAttributes> GetFileByStatus(String ownerID, boolean isOpend){
        return fileNet.getFilesByStatus(ownerID, isOpend);
    }
}
