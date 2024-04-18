package com.example.gateway.controller;

import com.example.gateway.enities.ClassificationFolderAttributes;
import com.example.gateway.enities.UserArchivingFolderAttributes;
import com.example.gateway.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/file")
//@RequiredArgsConstructor
public class FilesController {

    @Autowired
    private final FileService fileService;

    public FilesController() {
        fileService = null;
    }

    @GetMapping()
    public ArrayList<ClassificationFolderAttributes> getLeafFoldersByOwnerID(@RequestParam  String ownerID){
        return fileService.getLeafFoldersByOwnerID(ownerID);
    }

    @PostMapping()
    public void createArchive(@RequestBody UserArchivingFolderAttributes folderAttributes){
        System.out.println(folderAttributes);
        fileService.createArchive(folderAttributes);
    }

    @PostMapping("/correspondence")
    public void createCorrespondence(){
        fileService.createCorrespondence();
    }

    @PostMapping("/document")
    public void createDocument(){
        fileService.createDocument();
    }

    @GetMapping("/ownerId")
    public ArrayList<UserArchivingFolderAttributes> GetFilesByOwnerId(@RequestParam String ownerID){
        return fileService.GetFilesByOwnerId(ownerID);
    }

    @GetMapping("/byStatus")
    public ArrayList<UserArchivingFolderAttributes> GetFileByStatus(@RequestParam String ownerID, @RequestParam boolean isOpend){
        return fileService.GetFileByStatus(ownerID, isOpend);
    }

    @GetMapping("/countsByStatus")
    public void GetNumbersOfCorrespondenceByStatus(){
        fileService.GetNumbersOfCorrespondenceByStatus();
    }

    @GetMapping("/correspondenceByFileId")
    public void getCorrespondenceByFileID(){
        fileService.getCorrespondenceByFileID();
    }

    @PutMapping()
    public void updateFileStatus(@RequestParam String folderID, @RequestParam boolean isOpend){
        fileService.updateFileStatus(folderID, isOpend);
    }

    @DeleteMapping()
    public void deleteFileById(@RequestParam String folderID){
        fileService.deleteFileById(folderID);
    }
}
