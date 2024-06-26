package com.example.gateway.controller;

import com.example.gateway.DTOs.ClassificationFolderDTO;
import com.example.gateway.DTOs.UserArchivingFolderDTO;
import com.example.gateway.enities.CorrespondenceAttribute;
import com.example.gateway.enities.CrsClassifcation;
import com.example.gateway.enities.CrsDto;
import com.example.gateway.enities.UserArchivingFolderAttributes;
import com.example.gateway.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FilesController {

    @Autowired
    private final FileService fileService;

    public FilesController() {
        fileService = null;
    }

    @GetMapping()
    public ResponseEntity<?> getClassificationsFolderByOwnerID(@RequestParam String organization, @RequestParam String filterStr) {
        assert fileService != null;
        List<ClassificationFolderDTO> responseObject = fileService.getClassificationsFolderByOwnerID(organization, filterStr);
        return new ResponseEntity<>(responseObject, HttpStatus.ACCEPTED);
    }

//    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping()
    public ResponseEntity<?> createArchive(@RequestBody UserArchivingFolderAttributes folderAttributes){
        System.out.println(folderAttributes);
        assert fileService != null;
        UserArchivingFolderDTO responseObject =fileService.createArchive(folderAttributes);
        return new ResponseEntity<>(responseObject,HttpStatus.ACCEPTED);
    }

    @GetMapping("/ownerId")
    public ResponseEntity<?> GetFilesByOwnerId(@RequestParam String ownerID, @RequestParam String filterStr ){
        assert fileService != null;
        List<UserArchivingFolderDTO> responseObject = fileService.getUserFoldersByOwnerID(ownerID, filterStr);
        return new ResponseEntity<>(responseObject,HttpStatus.ACCEPTED);
    }

    @GetMapping("/byStatus")
    public ResponseEntity<?> GetFileByStatus(@RequestParam String ownerID, @RequestParam boolean isOpened){
        assert fileService != null;
        List<UserArchivingFolderDTO> responseObject = fileService.getUserFoldersByStatus(ownerID, isOpened);
        return new ResponseEntity<>(responseObject,HttpStatus.ACCEPTED);
    }

    @PostMapping("/correspondence")
    public ResponseEntity<?> createCorrespondenceDoc(@RequestBody ArrayList<CorrespondenceAttribute> correspondenceAttributes) {
        assert fileService != null;
        fileService.createCorrespondenceDoc(correspondenceAttributes);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/crsClassification")
    public ResponseEntity<?> getCrsClassification(){
        List<CrsClassifcation> responseObject = fileService.getCrsClassification();
        return new ResponseEntity<>(responseObject,HttpStatus.ACCEPTED);
    }
    @PostMapping("/document")
    public ResponseEntity<?> createDocument(){
        assert fileService != null;
        fileService.createDocument();
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/countsByStatus")
    public ResponseEntity<?> GetNumbersOfCorrespondenceByStatus(){
        fileService.GetNumbersOfCorrespondenceByStatus();
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/correspondenceByFileId")
    public ResponseEntity<?> getCorrespondenceByFileID(@RequestParam String fileID){
        List<CrsDto> responseObject = fileService.getCorrespondenceByFileID(fileID);
        return new ResponseEntity<>(responseObject,HttpStatus.ACCEPTED);
    }

    @PutMapping()
    public ResponseEntity<?> updateFileStatus(@RequestParam String folderID){
        fileService.updateFileStatus(folderID);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteFileById(@RequestParam String folderID){
        fileService.deleteFileById(folderID);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/crsCount")
    public ResponseEntity<?> getCrsCountByFileID(@RequestParam String fileID) {
        int response = fileService.getCrsCountByFileID(fileID);
        return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }

}
