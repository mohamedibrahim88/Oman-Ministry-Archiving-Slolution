package com.example.gateway.controller;

import com.example.gateway.DTOs.ClassificationFolderDTO;
import com.example.gateway.DTOs.UserArchivingFolderDTO;
import com.example.gateway.enities.CorrespondenceAttribute;
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

    @GetMapping("/ownerId")
    public ArrayList<UserArchivingFolderDTO> GetFilesByOwnerId(@RequestParam String ownerID, @RequestParam String filterStr ){
        assert fileService != null;
        return fileService.getUserFoldersByOwnerID(ownerID, filterStr);
    }

    @GetMapping("/byStatus")
    public ArrayList<UserArchivingFolderDTO> GetFileByStatus(@RequestParam String ownerID, @RequestParam boolean isOpened){
        assert fileService != null;
        return fileService.getUserFoldersByStatus(ownerID, isOpened);
    }

    @PostMapping("/correspondence")
    public void createCorrespondenceDoc(@RequestBody ArrayList<CorrespondenceAttribute> correspondenceAttributes) {
        assert fileService != null;
        fileService.createCorrespondenceDoc(correspondenceAttributes);
    }

    @PostMapping("/document")
    public void createDocument(){
        assert fileService != null;
        fileService.createDocument();
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
