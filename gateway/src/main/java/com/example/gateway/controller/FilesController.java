package com.example.gateway.controller;

import com.example.gateway.DTOs.ClassificationFolderDTO;
import com.example.gateway.DTOs.UserArchivingFolderDTO;
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
    public ArrayList<ClassificationFolderDTO> getClassificationsFolderByOwnerID(@RequestParam String organization, @RequestParam String filterStr) {
        assert fileService != null;
        return fileService.getClassificationsFolderByOwnerID(organization, filterStr);
    }

//    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping()
    public UserArchivingFolderDTO createArchive(@RequestBody UserArchivingFolderAttributes folderAttributes){
        System.out.println(folderAttributes);
        assert fileService != null;
        return fileService.createArchive(folderAttributes);
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
    public void updateFileStatus(@RequestParam String folderID){
        fileService.updateFileStatus(folderID);
    }

    @DeleteMapping()
    public void deleteFileById(@RequestParam String folderID){
        fileService.deleteFileById(folderID);
    }


}
