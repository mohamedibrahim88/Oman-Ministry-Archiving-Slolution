package com.example.gateway.client;

import com.example.gateway.DTOs.ClassificationFolderDTO;
import com.example.gateway.DTOs.UserArchivingFolderDTO;
import com.example.gateway.constants.*;
import com.example.gateway.enities.*;
import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.admin.PropertyTemplate;
import com.filenet.api.collection.*;
import com.filenet.api.constants.*;
import com.filenet.api.core.*;
import com.filenet.api.meta.PropertyDescription;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;
import org.apache.commons.collections4.IteratorUtils;
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
            throw new RuntimeException(e1.getMessage());
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
//        Calendar myCalendar = Calendar.getInstance();
//        myCalendar.add(Calendar.YEAR, Integer.parseInt(parentProp.getStringValue(ClassificationFolder.progressDuration.toString()).split("\\.")[0]));
//        Date progressEndDate = myCalendar.getTime();
//        myCalendar.add(Calendar.YEAR, Integer.parseInt(parentProp.getStringValue(ClassificationFolder.intermediateDuration.toString()).split("\\.")[0]));
//        Date intermediateEndDate = myCalendar.getTime();

        p.putValue(Structures.code.toString(),folderProp.getCode());
        p.putValue(Structures.arName.toString(), folderProp.getArName());
        p.putValue(Structures.enName.toString(),folderProp.getEnName());
        p.putValue(UserArchivingFolder.ownerID.toString(), folderProp.getOwnerID());
        p.putValue(Structures.level.toString(),String.valueOf(level));
        p.putValue(UserArchivingFolder.isOpened.toString(),Boolean.TRUE);
        p.putValue(UserArchivingFolder.retentionStatus.toString(), RetentionStatus.ACTIVE.toString());
        p.putValue(ClassificationFolder.progressDuration.toString(),parentProp.getStringValue(ClassificationFolder.progressDuration.toString()));
        p.putValue(ClassificationFolder.intermediateDuration.toString(),parentProp.getStringValue(ClassificationFolder.intermediateDuration.toString()));
        p.putValue(ClassificationFolder.finalDetermination.toString(),parentProp.getStringValue(ClassificationFolder.finalDetermination.toString()));
        myFolder.set_CmRetentionDate(RetentionConstants.INDEFINITE);
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
                "[isOpened] = " + isOpened + " AND [retentionStatus] = '" + RetentionStatus.ACTIVE.toString() + "' OPTIONS(TIMELIMIT 180)";

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
            userArchivingFolderDTO1.setProgressDuration(folderProp.getStringValue(ClassificationFolder.progressDuration.toString()));
            userArchivingFolderDTO1.setIntermediateDuration(folderProp.getStringValue(ClassificationFolder.intermediateDuration.toString()));
            userArchivingFolderDTO1.setFinalDetermination(folderProp.getStringValue(ClassificationFolder.finalDetermination.toString()));

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
                "[FolderName] like '" + filterStr + "%' AND [retentionStatus] = '" + RetentionStatus.ACTIVE.toString() + "'" +
                " OPTIONS(TIMELIMIT 180)";

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
            userArchivingFolderDTO1.setProgressDuration(folderProp.getStringValue(ClassificationFolder.progressDuration.toString()));
            userArchivingFolderDTO1.setIntermediateDuration(folderProp.getStringValue(ClassificationFolder.intermediateDuration.toString()));
            userArchivingFolderDTO1.setFinalDetermination(folderProp.getStringValue(ClassificationFolder.finalDetermination.toString()));

            userArchivingFolderDTO1.setClassificationEnName(parentFolderProp.getStringValue(Structures.enName.toString()));
            userArchivingFolderDTO1.setClassificationArName(parentFolderProp.getStringValue(Structures.arName.toString()));

            userArchivingFolderDTO.add(userArchivingFolderDTO1);
        }
        return userArchivingFolderDTO;
    }

    public void deleteFolderByID(String folderID){
        ObjectStore objectStore = getObjectStore(getCEConnection());
        Folder paretntFolder = Factory.Folder.fetchInstance(objectStore, new Id(folderID), null);
        FolderSet folderSet = paretntFolder.get_SubFolders();

        Iterator it = folderSet.iterator();

        while (it.hasNext()){
            Folder folder = (Folder) it.next();
            folder.delete();
            folder.save(RefreshMode.REFRESH);
         }
        paretntFolder.delete();

        paretntFolder.save(RefreshMode.REFRESH);
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

    public void createCorrespondenceDoc(ArrayList<CorrespondenceAttribute> correspondenceAttribute, String archiveFolderID) {
        //////////////////CREATE CORRESPONDENCE FOLDER
        ObjectStore objectStore = getObjectStore(getCEConnection());
        Folder parentFolder = Factory.Folder.fetchInstance(objectStore, new Id(archiveFolderID), null);
        Properties parentProp = parentFolder.getProperties();
        setCmRetention(parentFolder,null,null,RetentionCases.ArchiveFolder.getCases());
        for (int y = 0; y < correspondenceAttribute.size(); y++) {
            Folder myFolder = Factory.Folder.createInstance(objectStore, CorrespondenceFolder.correspondenceFolder.toString());
            Properties p1 = myFolder.getProperties();
            myFolder.set_Parent(parentFolder);
            myFolder.set_FolderName(correspondenceAttribute.get(y).getCorrespondenceID());
            // myFolder.set_Owner(folderProp.getOwners());
            // p.putValue("id",new Id(folderProp.getFolderID()));
            // p.putValue("code",folderProp.getCode());
            p1.putValue(UserArchivingFolder.ownerID.toString(), correspondenceAttribute.get(y).getUserID());
            p1.putValue(CorrespondenceFolder.CorrespondenceID.toString(), correspondenceAttribute.get(y).getCorrespondenceID());
            p1.putValue(UserArchivingFolder.isOpened.toString(),parentProp.getBooleanValue(UserArchivingFolder.isOpened.toString()));
            p1.putValue(CorrespondenceFolder.CorrespondenceID.toString(), correspondenceAttribute.get(y).getCorrespondenceID());
            p1.putValue(CorrespondenceFolder.Subject.toString(), correspondenceAttribute.get(y).getSubject());
            StringList senders1 = Factory.StringList.createList();
            boolean b0 = senders1.addAll(correspondenceAttribute.get(y).getSenders());
            p1.putValue(CorrespondenceFolder.Senders.toString(), senders1);

            StringList recievers1 = Factory.StringList.createList();
            boolean b12 = recievers1.addAll(correspondenceAttribute.get(y).getRecievers());
            p1.putValue(CorrespondenceFolder.Recievers.toString(), recievers1);

            StringList cc1 = Factory.StringList.createList();
            boolean b22 = cc1.addAll(correspondenceAttribute.get(y).getCc());
            p1.putValue(CorrespondenceFolder.CC.toString(), cc1);

            StringList bcc1 = Factory.StringList.createList();
            boolean b33 = bcc1.addAll(correspondenceAttribute.get(y).getBcc());
            p1.putValue(CorrespondenceFolder.BCC.toString(), bcc1);
            //        StringList owners = Factory.StringList.createList();
//        boolean b = owners.addAll(folderProp.getOwners());
//        p.putValue("Owners", owners);
            setCmRetention(parentFolder,myFolder,null,RetentionCases.CRSFolder.getCases());


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
            String docClass = correspondenceAttribute.get(y).getClassification();
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
                //p.putValue(CorrespondenceDocument.DocumentTitle.toString(),correspondenceAttribute.get(y).getDocTitle());
                p.putValue(CorrespondenceFolder.CorrespondenceID.toString(), correspondenceAttribute.get(y).getCorrespondenceID());
                p.putValue(CorrespondenceFolder.Subject.toString(), correspondenceAttribute.get(y).getSubject());
                p.putValue(CorrespondenceDocument.documentType.toString(),DocumentType.CRS.toString());
                StringList senders = Factory.StringList.createList();
                boolean b = senders.addAll(correspondenceAttribute.get(y).getSenders());
                p.putValue(CorrespondenceFolder.Senders.toString(), senders);

                StringList recievers = Factory.StringList.createList();
                boolean b1 = recievers.addAll(correspondenceAttribute.get(y).getRecievers());
                p.putValue(CorrespondenceFolder.Recievers.toString(), recievers);

                StringList cc = Factory.StringList.createList();
                boolean b2 = cc.addAll(correspondenceAttribute.get(y).getCc());
                p.putValue(CorrespondenceFolder.CC.toString(),cc);

                StringList bcc = Factory.StringList.createList();
                boolean b3 = bcc.addAll(correspondenceAttribute.get(y).getBcc());
                p.putValue(CorrespondenceFolder.BCC.toString(), bcc);
                for (String key : correspondenceAttribute.get(y).getProp().keySet()) {
                    if (isValidDate(correspondenceAttribute.get(y).getProp().get(key).toString())) {
                        // System.out.println("IS Date :>" + correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getProp().get(key).getClass().isInstance(new Date()));
                        System.out.println("IS Date :>");
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(correspondenceAttribute.get(y).getProp().get(key).toString());
                        p.putValue(key, date);
                        setCmRetention(parentFolder,myFolder,doc,RetentionCases.Document.getCases());
                    } else {
                        System.out.println("IS NO  Date :>");
                        p.putValue(key, correspondenceAttribute.get(y).getProp().get(key).toString());
                        setCmRetention(parentFolder,myFolder,doc,RetentionCases.Document.getCases());

                    }
                }

//Stores above content to the folder
                ReferentialContainmentRelationship rc = folder.file(doc,
                        AutoUniqueName.AUTO_UNIQUE,
                        documentName,
                        DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
                rc.save(RefreshMode.NO_REFRESH);
            } catch (Exception e) {
                System.out.println("Error MSG FROM CROSS:" + e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
///////////////////////////////UPLOAD ATTACHMENTS////////////////////

                for (int i = 0; i < correspondenceAttribute.get(y).getAttachmentsAttributes().size(); i++) {
                    try {
                        //C:\\Users\\Administrator\\Desktop\\Maktaby\\maktaby\\TestAttachments\\Test1.txt"
                        //correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getPath()
                    File attachmentPath = new File("//C:\\Users\\Administrator\\Desktop\\Maktaby\\maktaby\\TestAttachments\\Test1.txt");

                    FileInputStream file = new FileInputStream(attachmentPath);
                    Document doc = Factory.Document.createInstance(objectStore, correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getClassification());
                        System.out.println("Attachment Classification  ...." + correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getClassification());
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
                      //  p.putValue(CorrespondenceDocument.DocumentTitle.toString(), correspondenceAttribute.get(y).getDocTitle());
                        p.putValue(CorrespondenceFolder.CorrespondenceID.toString(), correspondenceAttribute.get(y).getCorrespondenceID());
                        p.putValue(CorrespondenceFolder.Subject.toString(), correspondenceAttribute.get(y).getSubject());
                        p.putValue(CorrespondenceDocument.documentType.toString(),DocumentType.ATTACHMENT.toString());
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
                    // p.putValue("DocumentTitle",correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getDocTitle());
                    for (String key : correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getProp().keySet()) {
                        if (isValidDate(correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getProp().get(key).toString())) {
                           // System.out.println("IS Date :>" + correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getProp().get(key).getClass().isInstance(new Date()));
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(correspondenceAttribute.get(y).getProp().get(key).toString());
                            p.putValue(key,date);
                            setCmRetention(parentFolder,myFolder,doc,RetentionCases.Document.getCases());

                        } else {
                            System.out.println("IS NO  Date :>");

                            p.putValue(key, correspondenceAttribute.get(y).getAttachmentsAttributes().get(i).getProp().get(key).toString());
                            setCmRetention(parentFolder,myFolder,doc,RetentionCases.Document.getCases());

                        }
                    }
//                for (int j=0; j<correspondenceAttribute.getAttachmentsAttributes().get(i).getProp().size();j++) {
//                    p.putValue(correspondenceAttribute.getAttachmentsAttributes().get(i).getProp()., correspondenceAttribute.getCorrespondenceID());
//                }


//Stores above content to the folder
                    ReferentialContainmentRelationship rc = folder.file(doc,
                            AutoUniqueName.AUTO_UNIQUE,
                            documentName2,
                            DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
                    rc.save(RefreshMode.REFRESH);
                }
                    catch (Exception e) {
                        System.out.println("ERROR MSG FROM ATTACHMENTS" + e.getMessage());
                        throw new RuntimeException(e.getMessage());
                    }
            }
        }
    }

    public ArrayList<CrsClassifcation> getCrsClassification() {

        ArrayList<CrsClassifcation> crsClassifcationsList = new ArrayList<>();
        ArrayList<CrsClassificationAttribute> crsClassificanDefination= new ArrayList<>();
        ArrayList<CrsClassificationAttribute> crsClassificationDescription= new ArrayList<>();
        ArrayList<String> superClassProps = new ArrayList<>();
        ObjectStore objectStore = getObjectStore(getCEConnection());

        ClassDefinition doc = Factory.ClassDefinition.fetchInstance(objectStore,CorrespondenceDocument.Correspondence.toString(),null);

        PropertyDefinitionList superClassDef= doc.get_PropertyDefinitions();

        Iterator itSuper = superClassDef.iterator();
        while (itSuper.hasNext()) {
        //CrsClassificationAttribute superClassClassification = new CrsClassificationAttribute();

        PropertyDefinition superClassprop = (PropertyDefinition) itSuper.next();


        superClassProps.add(superClassprop.get_SymbolicName());

        }

      ClassDefinitionSet classDefSet=  doc.get_ImmediateSubclassDefinitions();

        Iterator it = classDefSet.iterator();
        while (it.hasNext()){
            CrsClassifcation crsClassifcation=  new CrsClassifcation();
            ArrayList<CrsClassificationAttribute> crsClassificationDefPops = new ArrayList<>();
            ClassDefinition classDef = (ClassDefinition) it.next();

           PropertyDefinitionList propertyDefList = classDef.get_PropertyDefinitions();
            Iterator itDef = propertyDefList.iterator();
           while (itDef.hasNext())
           {
               CrsClassificationAttribute crsClassificationDefinition = new CrsClassificationAttribute();

               PropertyDefinition propertyDefinition = (PropertyDefinition) itDef.next();
               //PropertyTemplate propertyTemplate= propertyDefinition.get_PropertyTemplate();
               if (!propertyDefinition.get_IsSystemOwned() && !superClassProps.contains(propertyDefinition.get_SymbolicName()) )
               {
                crsClassificationDefinition.setDisplayName(propertyDefinition.get_DisplayName());
                crsClassificationDefinition.setSympolicName(propertyDefinition.get_SymbolicName());
                crsClassificationDefPops.add(crsClassificationDefinition);

               }
           }

            crsClassifcation.getCrsClassification().setDisplayName(classDef.get_DisplayName());
            crsClassifcation.getCrsClassification().setSympolicName(classDef.get_SymbolicName());
            crsClassifcation.getCrsClassificationProperties().addAll(crsClassificationDefPops);
            crsClassifcationsList.add(crsClassifcation);
            System.out.println("Class display name"+ classDef.get_DisplayName());

            System.out.println("object"+ crsClassifcation);

            System.out.println("list  "+ crsClassifcationsList);

            System.out.println("CRS Classification Definition " + crsClassificationDefPops);


        }
        return crsClassifcationsList;
    }


    public  ArrayList<CrsDto> getCrsByFileId (String fileID){
        ArrayList<CrsDto> crsDtos = new ArrayList<>();
        ObjectStore objectStore= getObjectStore(getCEConnection());
        Folder parentFolder = Factory.Folder.fetchInstance(objectStore, new Id(fileID), null);

        FolderSet folderSet= parentFolder.get_SubFolders();

        Iterator it = folderSet.iterator();

        while (it.hasNext()){
            CrsDto crsProp = new CrsDto();
            Folder folder = (Folder) it.next();

            Properties prop = folder.getProperties();

          crsProp.setReferenceNumber(prop.getStringValue(CorrespondenceFolder.CorrespondenceID.toString()));
          crsProp.setSubject(prop.getStringValue(CorrespondenceFolder.Subject.toString()));
          crsProp.setOpened(prop.getBooleanValue(UserArchivingFolder.isOpened.toString()));
          crsDtos.add(crsProp);
        }


        return  crsDtos;
    }

    public  int getCrsCountFileId (String fileID){
        ObjectStore objectStore= getObjectStore(getCEConnection());
        Folder parentFolder = Factory.Folder.fetchInstance(objectStore, new Id(fileID), null);

        FolderSet folderSet= parentFolder.get_SubFolders();
        Iterator it = folderSet.iterator();
         int count = IteratorUtils.size(it);
//        while (it.hasNext()){
//            CrsDto crsProp = new CrsDto();
//            Folder folder = (Folder) it.next();
//
//
//        }
        return  count;
    }

    private void setCmRetention (Folder parentFolder, Folder crsFolder,Document doc, int cases) {

        Properties parentFolderProp = parentFolder.getProperties();
        String finalDetermination = parentFolderProp.getStringValue(ClassificationFolder.finalDetermination.toString());
        String progressDuration =  parentFolderProp.getStringValue(ClassificationFolder.progressDuration.toString());
        String intermediateDuration =  parentFolderProp.getStringValue(ClassificationFolder.intermediateDuration.toString());
        RetentionEndDates retentionEndDates = getRetentionEndDate(progressDuration,intermediateDuration);
        if (cases == RetentionCases.ArchiveFolder.getCases()) {
            if (finalDetermination.equals(FinalDetermination.SELECTION.toString())) {
                parentFolderProp.putValue(UserArchivingFolder.progressEndDate.toString(),retentionEndDates.getProgressEndDate());
                parentFolderProp.putValue(UserArchivingFolder.intermediateEndDate.toString(),retentionEndDates.getIntermediateEndDate());
                parentFolder.set_CmRetentionDate(retentionEndDates.getIntermediateEndDate());
                parentFolder.save(RefreshMode.REFRESH);
            }
            if (finalDetermination.equals(FinalDetermination.DELETE.toString())) {
                parentFolderProp.putValue(UserArchivingFolder.progressEndDate.toString(),retentionEndDates.getProgressEndDate());
                parentFolderProp.putValue(UserArchivingFolder.intermediateEndDate.toString(),retentionEndDates.getIntermediateEndDate());
                parentFolder.set_CmRetentionDate(retentionEndDates.getIntermediateEndDate());
                parentFolder.save(RefreshMode.REFRESH);
            }
            if (finalDetermination.equals(FinalDetermination.PERMANENT.toString())) {
                parentFolderProp.putValue(UserArchivingFolder.progressEndDate.toString(),retentionEndDates.getProgressEndDate());
                parentFolderProp.putValue(UserArchivingFolder.intermediateEndDate.toString(),retentionEndDates.getIntermediateEndDate());
//                parentFolder.set_CmRetentionDate(RetentionConstants.PERMANENT);
                parentFolder.set_CmRetentionDate(retentionEndDates.getIntermediateEndDate());
                parentFolder.save(RefreshMode.REFRESH);
            }
        }
       else if (cases == RetentionCases.CRSFolder.getCases())
        {
            Properties crsProp = crsFolder.getProperties();
            if (finalDetermination.equals(FinalDetermination.SELECTION.toString())) {
                crsProp.putValue(UserArchivingFolder.progressEndDate.toString(),retentionEndDates.getProgressEndDate());
                crsProp.putValue(UserArchivingFolder.intermediateEndDate.toString(),retentionEndDates.getIntermediateEndDate());
                crsFolder.set_CmRetentionDate(retentionEndDates.getIntermediateEndDate());
                crsFolder.save(RefreshMode.REFRESH);
            }
            if (finalDetermination.equals(FinalDetermination.DELETE.toString())) {
                crsProp.putValue(UserArchivingFolder.progressEndDate.toString(),retentionEndDates.getProgressEndDate());
                crsProp.putValue(UserArchivingFolder.intermediateEndDate.toString(),retentionEndDates.getIntermediateEndDate());
                crsFolder.set_CmRetentionDate(retentionEndDates.getIntermediateEndDate());
                crsFolder.save(RefreshMode.REFRESH);
            }
            if (finalDetermination.equals(FinalDetermination.PERMANENT.toString())) {
                crsProp.putValue(UserArchivingFolder.progressEndDate.toString(),retentionEndDates.getProgressEndDate());
                crsProp.putValue(UserArchivingFolder.intermediateEndDate.toString(),retentionEndDates.getIntermediateEndDate());
//                crsFolder.set_CmRetentionDate(RetentionConstants.PERMANENT);
                crsFolder.set_CmRetentionDate(retentionEndDates.getIntermediateEndDate());

                crsFolder.save(RefreshMode.REFRESH);
            }

        } else if (cases == RetentionCases.Document.getCases()) {
            Properties docProp = doc.getProperties();
            if (finalDetermination.equals(FinalDetermination.SELECTION.toString())){
                doc.set_CmRetentionDate(retentionEndDates.getIntermediateEndDate());
                doc.save(RefreshMode.REFRESH);
            } else if (finalDetermination.equals(FinalDetermination.DELETE.toString())) {
                doc.set_CmRetentionDate(retentionEndDates.getIntermediateEndDate());
                doc.save(RefreshMode.REFRESH);
            } else if (finalDetermination.equals(FinalDetermination.PERMANENT.toString())) {
               // doc.set_CmRetentionDate(RetentionConstants.PERMANENT);
                doc.set_CmRetentionDate(retentionEndDates.getIntermediateEndDate());

                doc.save(RefreshMode.REFRESH);
            }

        }

    }

    private RetentionEndDates getRetentionEndDate(String progressDuration, String intermediateDuration)
    {
        RetentionEndDates retentionEndDates = new RetentionEndDates();
        Calendar  intermediateCalendar = Calendar.getInstance();
        Calendar progressCalendar = Calendar.getInstance();
        intermediateCalendar.add(Calendar.DATE, Integer.parseInt(progressDuration.split("\\.")[0])+Integer.parseInt(intermediateDuration.split("\\.")[0]));
        retentionEndDates.setIntermediateEndDate(intermediateCalendar.getTime());
        progressCalendar.add(Calendar.DATE,Integer.parseInt(progressDuration.split("\\.")[0]));
        retentionEndDates.setProgressEndDate(progressCalendar.getTime());
        return retentionEndDates;
    }
}





