package com.example.gateway.client;

import com.example.gateway.DTOs.ClassificationFolderDTO;
import com.example.gateway.DTOs.UserArchivingFolderDTO;
import com.example.gateway.constants.*;
import com.example.gateway.enities.CorrespondenceAttribute;
import com.example.gateway.enities.UserArchivingFolderAttributes;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.StringList;
import com.filenet.api.constants.*;
import com.filenet.api.core.*;
import com.filenet.api.property.Properties;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.MimetypesFileTypeMap;
import javax.security.auth.Subject;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class FileNet {

    @Value("${connection.file.net}")
    private String connectionFileNet;
    @Value("${connection.object.store}")
    private  String objectStoreName;
    @Value("${connection.username}")
    private String connectionUsername;
    @Value("${connection.password}")
    private String connectionPassword;

    public Connection getCEConnection () {
        Connection conn = null;
        try
        {
            String ceURI = connectionFileNet;
            String username = connectionUsername;
            String password = connectionPassword;

            if (conn == null){
                conn = Factory.Connection.getConnection(ceURI);

                Subject subject = UserContext.createSubject(conn, username, password, null);
                UserContext uc = UserContext.get();

                uc.pushSubject(subject);
            }
        }catch(Exception e1)
        {
            e1.printStackTrace();
        }
        System.out.println("CE Connection"+ conn);
        return conn ;
    }

    public ObjectStore getObjectStore(Connection conn) {
        Domain domain = Factory.Domain.fetchInstance(conn, null, null);
        ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, objectStoreName, null);
        return objectStore;
    }

    public UserArchivingFolderDTO createArchive(UserArchivingFolderAttributes folderProp){

        ObjectStore objectStore = getObjectStore(getCEConnection());
        Folder parentFolder = Factory.Folder.fetchInstance(objectStore,new Id(folderProp.getParentID()),null);
        Folder myFolder = Factory.Folder.createInstance(objectStore,UserArchivingFolder.userArchivingFolder.toString());
        Properties parentProp = parentFolder.getProperties();
        Properties p = myFolder.getProperties();
        myFolder.set_Parent(parentFolder);
        myFolder.set_FolderName(folderProp.getArName()+"-"+folderProp.getCode());

        int level = Integer.parseInt(parentProp.getStringValue(Structures.level.toString()).split("\\.")[0]) + 1;
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.add(Calendar.YEAR, Integer.parseInt(parentProp.getStringValue(ClassificationFolder.progressDuration.toString()).split("\\.")[0]));
        Date progressEndDate = myCalendar.getTime();
        myCalendar.add(Calendar.YEAR, Integer.parseInt(parentProp.getStringValue(ClassificationFolder.intermediateDuration.toString()).split("\\.")[0]));
        Date intermediateEndDate = myCalendar.getTime();

        p.putValue(Structures.code.toString(),folderProp.getCode());
        p.putValue(Structures.arName.toString(), folderProp.getArName());
        p.putValue(Structures.enName.toString(),folderProp.getEnName());
        p.putValue(UserArchivingFolder.ownerID.toString(), folderProp.getOwnerID());
        p.putValue(Structures.level.toString(),String.valueOf(level));
        p.putValue(UserArchivingFolder.isOpened.toString(),Boolean.TRUE);
        p.putValue(UserArchivingFolder.progressEndDate.toString(), progressEndDate);
        p.putValue(UserArchivingFolder.intermediateEndDate.toString(), intermediateEndDate);
//        p.putValue(ClassificationFolder.progressDuration.toString(),parentProp.getStringValue(ClassificationFolder.progressDuration.toString()));
//        p.putValue(ClassificationFolder.intermediateDuration.toString(),parentProp.getStringValue(ClassificationFolder.intermediateDuration.toString()));
//        p.putValue(ClassificationFolder.finalDetermination.toString(),parentProp.getStringValue(ClassificationFolder.finalDetermination.toString()));

        myFolder.save(RefreshMode.REFRESH);

//        String parentFolderPath =  parentFolder.get_PathName();
//        Folder archivingFolder = Factory.Folder.fetchInstance(objectStore, parentFolderPath + "\\" + folderProp.getArName()+"-"+folderProp.getCode(), null);
//        Properties archivingFolderProperties = archivingFolder.getProperties();
//        String folderID = archivingFolderProperties.getIdValue("id").toString();
        String folderID = myFolder.get_Id().toString();
        folderID = folderID.replace("{","");
        folderID = folderID.replace("}", "");

        UserArchivingFolderDTO userArchivingFolderDTO = new UserArchivingFolderDTO();
        userArchivingFolderDTO.setArName(folderProp.getArName());
        userArchivingFolderDTO.setEnName(folderProp.getEnName());
        userArchivingFolderDTO.setOwnerID(folderProp.getOwnerID());
        userArchivingFolderDTO.setFolderID(folderID);
        userArchivingFolderDTO.setCode(folderProp.getCode());
        userArchivingFolderDTO.setLevel(String.valueOf(level));
        userArchivingFolderDTO.setOpened(Boolean.TRUE);
        userArchivingFolderDTO.setClassificationArName(parentProp.getStringValue(Structures.arName.toString()));
        userArchivingFolderDTO.setClassificationEnName(parentProp.getStringValue(Structures.enName.toString()));
        userArchivingFolderDTO.setProgressDuration(parentProp.getStringValue(ClassificationFolder.progressDuration.toString()));
        userArchivingFolderDTO.setIntermediateDuration(parentProp.getStringValue(ClassificationFolder.intermediateDuration.toString()));
        userArchivingFolderDTO.setFinalDetermination(parentProp.getStringValue(ClassificationFolder.finalDetermination.toString()));

        return userArchivingFolderDTO;
    }

    public ArrayList<ClassificationFolderDTO> getClassificationsFolderByOwnerID(String organization, String filterStr) {
        ObjectStore objectStore = getObjectStore(getCEConnection());
        ArrayList<ClassificationFolderDTO> classificationFolderDTO = new ArrayList<>();
        SearchScope search = new SearchScope(objectStore);

        String mySQL = "SELECT [This], [FolderName], [Id], [arName], [code], [enName], " +
                "[finalDetermination], [intermediateDuration], [level], [progressDuration], " +
                "[ruleNumber] FROM [" + ClassificationFolder.classificationFolders.toString() +
                "] WHERE '" + organization + "' in [Groups] AND " +
                "[FolderName] like '" + filterStr + "%' OPTIONS(TIMELIMIT 180)";

        SearchSQL sql = new SearchSQL(mySQL);
        FolderSet folders = (FolderSet) search.fetchObjects(sql, Integer.valueOf("500"), null, Boolean.TRUE);
        Iterator it1 = folders.iterator();
        while (it1.hasNext()) {
            Folder folder = (Folder) it1.next();
            Properties folderProp = folder.getProperties();
            ClassificationFolderDTO classificationFolderDTO1 = new ClassificationFolderDTO();

            classificationFolderDTO1.setFolderID(folderProp.getIdValue(Structures.id.toString()).toString());
            classificationFolderDTO1.setArName(folderProp.getStringValue(Structures.arName.toString()));
            classificationFolderDTO1.setEnName(folderProp.getStringValue(Structures.enName.toString()));
            classificationFolderDTO1.setCode(folderProp.getStringValue(Structures.code.toString()));
            classificationFolderDTO1.setProgressDuration(folderProp.getStringValue(ClassificationFolder.progressDuration.toString()));
            classificationFolderDTO1.setIntermediateDuration(folderProp.getStringValue(ClassificationFolder.intermediateDuration.toString()));
            classificationFolderDTO1.setFinalDetermination(folderProp.getStringValue(ClassificationFolder.finalDetermination.toString()));

            classificationFolderDTO.add(classificationFolderDTO1);
        }

        return classificationFolderDTO;
    }

    public ArrayList<UserArchivingFolderDTO> getUserFoldersByStatus(String ownerID, boolean isOpened) {
        ObjectStore objectStore = getObjectStore(getCEConnection());
        ArrayList<UserArchivingFolderDTO> userArchivingFolderDTO = new ArrayList<>();
        SearchScope search = new SearchScope(objectStore);
        String mySQL;
        mySQL = "SELECT [This], [FolderName], [Id], [arName], [code], [enName], " +
                "[finalDetermination], [intermediateDuration], [level], [progressDuration], " +
                "[ruleNumber], [ownerID], [isOpened] FROM [" + UserArchivingFolder.userArchivingFolder.toString() +
                "] WHERE [ownerID] = '" + ownerID + "' AND " +
                "[isOpened] = " + isOpened + " OPTIONS(TIMELIMIT 180)";

        SearchSQL sql = new SearchSQL(mySQL);
        FolderSet folders = (FolderSet) search.fetchObjects(sql, Integer.valueOf("500"), null, Boolean.TRUE);
        Iterator it1 = folders.iterator();
        while (it1.hasNext()) {
            Folder folder = (Folder) it1.next();
            Properties folderProp = folder.getProperties();
            Folder userArchivingFolder = Factory.Folder.fetchInstance(objectStore, new Id(folderProp.getIdValue(Structures.id.toString()).toString()), null);
            Folder parentFolder = userArchivingFolder.get_Parent();
            Properties parentFolderProp = parentFolder.getProperties();
            UserArchivingFolderDTO userArchivingFolderDTO1= new UserArchivingFolderDTO();

            userArchivingFolderDTO1.setFolderID(folderProp.getIdValue(Structures.id.toString()).toString());
            userArchivingFolderDTO1.setArName(folderProp.getStringValue(Structures.arName.toString()));
            userArchivingFolderDTO1.setEnName(folderProp.getStringValue(Structures.enName.toString()));
            userArchivingFolderDTO1.setCode(folderProp.getStringValue(Structures.code.toString()));
            userArchivingFolderDTO1.setLevel(folderProp.getStringValue(Structures.level.toString()));
            userArchivingFolderDTO1.setOwnerID(folderProp.getStringValue(UserArchivingFolder.ownerID.toString()));
            userArchivingFolderDTO1.setOpened(folderProp.getBooleanValue(UserArchivingFolder.isOpened.toString()));

            userArchivingFolderDTO1.setProgressDuration(parentFolderProp.getStringValue(ClassificationFolder.progressDuration.toString()));
            userArchivingFolderDTO1.setIntermediateDuration(parentFolderProp.getStringValue(ClassificationFolder.intermediateDuration.toString()));
            userArchivingFolderDTO1.setFinalDetermination(parentFolderProp.getStringValue(ClassificationFolder.finalDetermination.toString()));
            userArchivingFolderDTO1.setClassificationEnName(parentFolderProp.getStringValue(Structures.enName.toString()));
            userArchivingFolderDTO1.setClassificationArName(parentFolderProp.getStringValue(Structures.arName.toString()));

            userArchivingFolderDTO.add(userArchivingFolderDTO1);
        }
        return userArchivingFolderDTO;
    }

    public ArrayList<UserArchivingFolderDTO> getUserFoldersByOwnerID(String ownerID, String filterStr) {
        ObjectStore objectStore = getObjectStore(getCEConnection());
        ArrayList<UserArchivingFolderDTO> userArchivingFolderDTO = new ArrayList<>();
        SearchScope search = new SearchScope(objectStore);
        String mySQL;
        mySQL = "SELECT [This], [FolderName], [Id], [arName], [code], [enName], " +
                "[finalDetermination], [intermediateDuration], [level], [progressDuration], " +
                "[ruleNumber], [ownerID], [isOpened] FROM [" + UserArchivingFolder.userArchivingFolder.toString() +
                "] WHERE [ownerID] = '" + ownerID + "' AND " +
                "[FolderName] like '" + filterStr + "%' OPTIONS(TIMELIMIT 180)";

        SearchSQL sql = new SearchSQL(mySQL);
        FolderSet folders = (FolderSet) search.fetchObjects(sql, Integer.valueOf("500"), null, Boolean.TRUE);
        Iterator it1 = folders.iterator();
        while (it1.hasNext()) {
            Folder folder = (Folder) it1.next();
            Properties folderProp = folder.getProperties();
            Folder userArchivingFolder = Factory.Folder.fetchInstance(objectStore, new Id(folderProp.getIdValue(Structures.id.toString()).toString()), null);
            Folder parentFolder = userArchivingFolder.get_Parent();
            Properties parentFolderProp = parentFolder.getProperties();
            UserArchivingFolderDTO userArchivingFolderDTO1= new UserArchivingFolderDTO();

            userArchivingFolderDTO1.setFolderID(folderProp.getIdValue(Structures.id.toString()).toString());
            userArchivingFolderDTO1.setArName(folderProp.getStringValue(Structures.arName.toString()));
            userArchivingFolderDTO1.setEnName(folderProp.getStringValue(Structures.enName.toString()));
            userArchivingFolderDTO1.setCode(folderProp.getStringValue(Structures.code.toString()));
            userArchivingFolderDTO1.setLevel(folderProp.getStringValue(Structures.level.toString()));
            userArchivingFolderDTO1.setOwnerID(folderProp.getStringValue(UserArchivingFolder.ownerID.toString()));
            userArchivingFolderDTO1.setOpened(folderProp.getBooleanValue(UserArchivingFolder.isOpened.toString()));

            userArchivingFolderDTO1.setProgressDuration(parentFolderProp.getStringValue(ClassificationFolder.progressDuration.toString()));
            userArchivingFolderDTO1.setIntermediateDuration(parentFolderProp.getStringValue(ClassificationFolder.intermediateDuration.toString()));
            userArchivingFolderDTO1.setFinalDetermination(parentFolderProp.getStringValue(ClassificationFolder.finalDetermination.toString()));
            userArchivingFolderDTO1.setClassificationEnName(parentFolderProp.getStringValue(Structures.enName.toString()));
            userArchivingFolderDTO1.setClassificationArName(parentFolderProp.getStringValue(Structures.arName.toString()));

            userArchivingFolderDTO.add(userArchivingFolderDTO1);
        }
        return userArchivingFolderDTO;
    }

    public void deleteFolderByID(String folderID){
        ObjectStore objectStore = getObjectStore(getCEConnection());
        Folder folder = Factory.Folder.fetchInstance(objectStore, new Id(folderID), null);
        folder.delete();
        folder.save(RefreshMode.NO_REFRESH);
    }

    public void updateFolderStatus(String folderID){
        ObjectStore objectStore = getObjectStore(getCEConnection());
        Folder folder = Factory.Folder.fetchInstance(objectStore, new Id(folderID), null);
        Properties p = folder.getProperties();
        p.putValue(UserArchivingFolder.isOpened.toString(), Boolean.FALSE);
        folder.save(RefreshMode.NO_REFRESH);
    }

    public  String getMimeType(String fileUrl) {

        MimetypesFileTypeMap mimeTypesMap  =  new MimetypesFileTypeMap();
        String mimeType = mimeTypesMap.getContentType(fileUrl);
        return mimeType;
    }

    public boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    /////////////////Create CRS//////////////////////

    public void createCorrespondenceDoc(ArrayList<CorrespondenceAttribute> correspondenceAttribute) {
        //////////////////CREATE CORRESPONDENCE FOLDER

        ObjectStore objectStore = getObjectStore(getCEConnection());
        for (int y = 0; y < correspondenceAttribute.size(); y++) {
            Folder parentFolder = Factory.Folder.fetchInstance(objectStore, new Id(correspondenceAttribute.get(y).getFolderID()), null);
            Folder myFolder = Factory.Folder.createInstance(objectStore, CorrespondenceFolder.correspondenceFolder.toString());
            Properties parentProp = parentFolder.getProperties();
            Properties p1 = myFolder.getProperties();
            myFolder.set_Parent(parentFolder);
            myFolder.set_FolderName(correspondenceAttribute.get(y).getCorrespondenceID());
            // myFolder.set_Owner(folderProp.getOwners());
            // p.putValue("id",new Id(folderProp.getFolderID()));
            // p.putValue("code",folderProp.getCode());
            p1.putValue(UserArchivingFolder.ownerID.toString(), correspondenceAttribute.get(y).getUserID());
            p1.putValue(CorrespondenceFolder.CorrespondenceID.toString(), correspondenceAttribute.get(y).getCorrespondenceID());

//        StringList owners = Factory.StringList.createList();
//        boolean b = owners.addAll(folderProp.getOwners());
//        p.putValue("Owners", owners);


            myFolder.save(RefreshMode.REFRESH);


            ///////////////CREATE DOCUMENT
//        Connection conn = getCEConnection();
//        Domain domain = Factory.Domain.fetchInstance(conn, null, null);
//        ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, "test", null);
            Document document = null;
            //Get Folder
            Folder folder = null;
            String folderName = myFolder.get_PathName();
            folder = Factory.Folder.fetchInstance(objectStore, folderName, null);

            File f = new File(correspondenceAttribute.get(y).getPath());

            //Get the File details
            String fileName = "";
            int fileSize = 0;

// Create Document
            System.out.println(correspondenceAttribute);
            String docClass = CorrespondenceDocument.Correspondence.toString();
            try {
                FileInputStream file = new FileInputStream(f);
                Document doc = Factory.Document.createInstance(objectStore, docClass);
                if (f.exists()) {
                    ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
                    ContentElementList contentElementList = Factory.ContentElement.createList();

                    contentTransfer.setCaptureSource(file);
                    contentElementList.add(contentTransfer);
                    doc.set_ContentElements(contentElementList);
                    contentTransfer.set_RetrievalName(correspondenceAttribute.get(y).getCorrespondenceID());
                    //   doc.set_Creator(correspondenceAttribute.get(y).getUserID());
                    doc.set_MimeType(getMimeType(correspondenceAttribute.get(y).getPath()));

                }


//Check-in the doc
                doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
//Get and put the doc properties
                String documentName = correspondenceAttribute.get(y).getCorrespondenceID();
                Properties p = doc.getProperties();
                // p.putValue("DocumentTitle",correspondenceAttribute.get(y).getDocTitle());
                p.putValue(CorrespondenceFolder.CorrespondenceID.toString(), correspondenceAttribute.get(y).getCorrespondenceID());
                p.putValue(CorrespondenceFolder.Subject.toString(), correspondenceAttribute.get(y).getSubject());
                StringList senders = Factory.StringList.createList();
                boolean b = senders.addAll(correspondenceAttribute.get(y).getSenders());
                p.putValue(CorrespondenceFolder.Senders.toString(), senders);

                StringList recievers = Factory.StringList.createList();
                boolean b1 = recievers.addAll(correspondenceAttribute.get(y).getRecievers());
                p.putValue(CorrespondenceFolder.Recievers.toString(), recievers);

                StringList cc = Factory.StringList.createList();
                boolean b2 = cc.addAll(correspondenceAttribute.get(y).getCc());
                p.putValue(CorrespondenceFolder.CC.toString(), cc);

                StringList bcc = Factory.StringList.createList();
                boolean b3 = bcc.addAll(correspondenceAttribute.get(y).getBcc());
                p.putValue(CorrespondenceFolder.BCC.toString(), bcc);
                doc.save(RefreshMode.REFRESH);

//Stores above content to the folder
                ReferentialContainmentRelationship rc = folder.file(doc,
                        AutoUniqueName.AUTO_UNIQUE,
                        documentName,
                        DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
                rc.save(RefreshMode.REFRESH);
            } catch (Exception e) {
                System.out.println("Error MSG FROM CROSS:" + e.getMessage());
            }
///////////////////////////////UPLOAD ATTACHMENTS////////////////////
            try {
                for (int i = 0; i < correspondenceAttribute.get(y).getAttachmentsAttributes().size(); i++) {
                    File attachmentPath = new File(correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getPath());

                    FileInputStream file = new FileInputStream(attachmentPath);
                    Document doc = Factory.Document.createInstance(objectStore, correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getClassification());
                    if (attachmentPath.exists()) {
                        ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
                        ContentElementList contentElementList = Factory.ContentElement.createList();

                        contentTransfer.setCaptureSource(file);
                        contentElementList.add(contentTransfer);
                        doc.set_ContentElements(contentElementList);
                        contentTransfer.set_RetrievalName(correspondenceAttribute.get(y).getCorrespondenceID());
                        doc.set_MimeType(getMimeType(correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getPath()));
                        //doc.set_Creator(correspondenceAttribute.get(y).getUserID());
                    }


//Check-in the doc
                    doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
//Get and put the doc properties
                    String documentName2 = correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getDocTitle();
                    Properties p = doc.getProperties();
                    // p.putValue("DocumentTitle",correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getDocTitle());
                    for (String key : correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getProp().keySet()) {
                        if (isValidDate(correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getProp().get(key).toString())) {
                           // System.out.println("IS Date :>" + correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getProp().get(key).getClass().isInstance(new Date()));
                            p.putValue(key, (Date) correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getProp().get(key));
                        } else {
                            System.out.println("IS NO  Date :>");

                            p.putValue(key, correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getProp().get(key).toString());

                        }
                    }
//                for (int j=0; j<correspondenceAttribute.getAttachmentsAttributes().get(i).getProp().size();j++) {
//                    p.putValue(correspondenceAttribute.getAttachmentsAttributes().get(i).getProp()., correspondenceAttribute.getCorrespondenceID());
//                }

                    doc.save(RefreshMode.REFRESH);

//Stores above content to the folder
                    ReferentialContainmentRelationship rc = folder.file(doc,
                            AutoUniqueName.AUTO_UNIQUE,
                            documentName2,
                            DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
                    rc.save(RefreshMode.REFRESH);
                }
            } catch (Exception e) {
                System.out.println("ERROR MSG FROM ATTACHMENTS" + e.getMessage());
            }
        }
    }
    }


