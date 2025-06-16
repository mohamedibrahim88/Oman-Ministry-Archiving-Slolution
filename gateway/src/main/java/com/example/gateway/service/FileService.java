package com.example.gateway.service;

import com.example.gateway.DTOs.ClassificationFolderDTO;
import com.example.gateway.DTOs.CorrespondenceFolderDTO;
import com.example.gateway.DTOs.UserArchivingFolderDTO;
import com.example.gateway.client.FileNet;
import com.example.gateway.enities.*;
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

    public CorrespondenceFolderDTO createCRSFolder(CorrespondenceFolderAttributes folderAttributes){
        return fileNet.createCRSFolder(folderAttributes);
    }

    public CorrespondenceFolderDTO isCRSFolderCreated(CorrespondenceFolderAttributes folderAttributes){
        return fileNet.isCRSFolderCreated(folderAttributes);
    }

    public void closeCRSFolder(ArrayList<CorrespondenceAttribute> correspondenceAttributes,String correspondenceFolderID){
        fileNet.closeCRSFolder(correspondenceAttributes,correspondenceFolderID);
    }

    public void addCRSDocuments(ArrayList<CorrespondenceAttribute> correspondenceAttributes,String correspondenceFolderID){
        fileNet.addCRSDocuments(correspondenceAttributes,correspondenceFolderID);
    }

    public void createCorrespondenceDoc(ArrayList<CorrespondenceAttribute> correspondenceAttributes,String folderID){
        fileNet.createCorrespondenceDoc(correspondenceAttributes,folderID);
    }

    public ArrayList<CrsClassifcation> getCrsClassification()
    {
        return fileNet.getCrsClassification();
    }
    public void createDocument(){

    }
    public ArrayList<UserArchivingFolderDTO> getUserFoldersByOwnerID(String ownerID, String filterStr){
        return fileNet.getUserFoldersByOwnerID(ownerID, filterStr);
    }
    public ArrayList<UserArchivingFolderDTO> getUserFoldersByStatus(String ownerID, boolean isOpened){
        return fileNet.getUserFoldersByStatus(ownerID, isOpened);
    }
    public ArrayList<CrsDto> getCorrespondenceByFileID(String fileId){
        return fileNet.getCrsByFileId(fileId);
    }
    public void updateFileStatus(String folderID){
        fileNet.updateFolderStatus(folderID);
    }
    public void deleteFileById(String folderID){
        fileNet.deleteFolderByID(folderID);
    }

    public void GetNumbersOfCorrespondenceByStatus(){

    }

    public int getCrsCountByFileID(String fileId)
    {
        return fileNet.getCrsCountFileId(fileId);
    }


}
