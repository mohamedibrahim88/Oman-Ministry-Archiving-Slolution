package com.example.gateway.client;

import com.example.gateway.enities.CorrespondenceAttribute;
import com.example.gateway.enities.FolderAttributes;
import com.example.gateway.enities.LeafFolderAttributes;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

@Component
public class FileNet {
    @Value("${connection.file.net}")
    private String connectionFileNet;
    @Value("${connection.object.store}")
    private  String objectStroreName;
    @Value("${connection.username}")
    private String connectionUsername;
    @Value("${connection.password}")
    private String connectionPassword;
    @Value("${folder.class.leaf}")
    private String leafFolderClass;
    public Connection getCEConnection () {
        Connection conn = null;
        try
        {
            String ceURI = connectionFileNet;
            String userName = connectionUsername;
            String password = connectionPassword;

            if (conn == null){
                conn = Factory.Connection.getConnection(ceURI);

                Subject subject = UserContext.createSubject(conn, userName, password, null);
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
        ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, objectStroreName, null);
        return objectStore;
    }
    public void createArchive(LeafFolderAttributes folderProp){

        ObjectStore objectStore = getObjectStore(getCEConnection());
        Folder parentFolder = Factory.Folder.fetchInstance(objectStore,new Id(folderProp.getParentID()),null);
        Folder myFolder = Factory.Folder.createInstance(objectStore,leafFolderClass);
        Properties parentProp = parentFolder.getProperties();
        Properties p = myFolder.getProperties();
        myFolder.set_Parent(parentFolder);
        myFolder.set_FolderName(folderProp.getArName()+"-"+folderProp.getCode());
        // myFolder.set_Owner(folderProp.getOwners());
        p.putValue("id",new Id(folderProp.getFolderID()));
        p.putValue("code",folderProp.getCode());
        p.putValue("arName", folderProp.getArName());
        p.putValue("enName",folderProp.getEnName());
        p.putValue("type",folderProp.getType());
        p.putValue("level",folderProp.getLevel());
        p.putValue("isOpened",folderProp.getOpend());
        p.putValue("ownerID", folderProp.getOwnerID());
        p.putValue("progressDuration",parentProp.getStringValue("progressDuration"));
        p.putValue("interDuration",parentProp.getStringValue("interDuration"));
        p.putValue("DurationUnit",parentProp.getStringValue("DurationUnit"));
        p.putValue("finalDeter",parentProp.getStringValue("finalDeter"));

//        p.putValue("Owners", parentProp.getStringListValue("Owners"));
//        StringList owners = Factory.StringList.createList();
//        boolean b = owners.addAll(folderProp.getOwners());
//        p.putValue("Owners", owners);


        myFolder.save(RefreshMode.NO_REFRESH);

    }

    public ArrayList<FolderAttributes> getLeafFoldersByOwnerID(String owners) {
        ObjectStore objectStore = getObjectStore(getCEConnection());
        ArrayList<FolderAttributes> folderAttributes = new ArrayList<>();
        SearchScope search = new SearchScope(objectStore);
        String mySQL = "SELECT [This], [ClassDescription], [CmIndexingFailureCode], [CmIsMarkedForDeletion]," +
                " [CmRetentionDate], [ContainerType], [Creator], [DateCreated], [DateLastModified], " +
                "[DurationUnit], [FinalDetermination], [FolderName], [Id], [IndexationId], " +
                "[InheritParentPermissions], [InprogressDuration], [IntermediateDuration], " +
                "[IsHiddenContainer], [LastModifier], [LockOwner], [LockTimeout], [LockToken], " +
                "[Name], [Owner], [PathName], [Owners], [arName], [code], [enName], [level], [type]  " +
                "FROM [structures] WHERE '" + owners  +"' in [Owners] OPTIONS(TIMELIMIT 180)";
        SearchSQL sql = new SearchSQL(mySQL);
        FolderSet folders = (FolderSet) search.fetchObjects(sql, Integer.valueOf("500"), null, Boolean.TRUE);
        Iterator it1 = folders.iterator();
        while (it1.hasNext()) {
            Folder folder = (Folder) it1.next();
            Properties folderProp = folder.getProperties();
            FolderAttributes folderProperties= new FolderAttributes();

            folderProperties.setFolderID(String.valueOf(folderProp.getIdValue("Id")));
            folderProperties.setParentID("No data");
            folderProperties.setArName(folderProp.getStringValue("ArName")) ;
            folderProperties.setEnName( folderProp.getStringValue("enName"));
            folderProperties.setCode( folderProp.getStringValue("code"));
            folderProperties.setLevel( folderProp.getStringValue("level"));
            folderProperties.setType( folderProp.getStringValue("type"));
            folderProperties.setProgressDuration( folderProp.getStringValue("InprogressDuration"));
            folderProperties.setInterDuration( folderProp.getStringValue("IntermediateDuration"));
            folderProperties.setDurationUnit( folderProp.getStringValue("DurationUnit"));
            folderProperties.setFinalDeter( folderProp.getStringValue("FinalDetermination"));

            StringList ownersList = folderProp.getStringListValue("Owners");
            String[] stringArray = (String[]) ownersList.toArray(new String[ownersList.size()]);
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(stringArray));
            folderProperties.setOwners(arrayList);

//            System.out.println(folderProperties.toString());
            folderAttributes.add(folderProperties);
        }
        System.out.println(folderAttributes);
        return folderAttributes;
    }

    public ArrayList<LeafFolderAttributes> getFilesByStatus (String ownerID, boolean isOpend) {
        ObjectStore objectStore = getObjectStore(getCEConnection());
        ArrayList<LeafFolderAttributes> folderAttributes = new ArrayList<>();
        SearchScope search = new SearchScope(objectStore);
        String mySQL;
        mySQL ="SELECT [This], [ClassDescription], [CmIndexingFailureCode], [CmIsMarkedForDeletion]," +
                " [CmRetentionDate], [ContainerType], [Creator], [DateCreated], [DateLastModified]," +
                " [DurationUnit], [FinalDetermination], [FolderName], [Id], [IndexationId]," +
                " [InheritParentPermissions], [InprogressDuration], [IntermediateDuration]," +
                " [IsHiddenContainer], [LastModifier], [LockOwner], [LockTimeout], [LockToken]," +
                " [Name], [Owner], [Owners], [PathName], [arName], [code], [enName], [isOpend], [level]," +
                " [ownerID], [type]  FROM [leaf] WHERE [ownerID] ='" + ownerID + "'and [isOpend] =" + isOpend +
                " OPTIONS(TIMELIMIT 180)";
        SearchSQL sql = new SearchSQL(mySQL);
        FolderSet folders = (FolderSet) search.fetchObjects(sql, Integer.valueOf("500"), null, Boolean.TRUE);
        Iterator it1 = folders.iterator();
        while (it1.hasNext()) {
            Folder folder = (Folder) it1.next();
            Properties folderProp = folder.getProperties();
            LeafFolderAttributes folderProperties= new LeafFolderAttributes();

            folderProperties.setFolderID(String.valueOf(folderProp.getIdValue("Id")));
            folderProperties.setParentID("No data");
            folderProperties.setArName(folderProp.getStringValue("ArName")) ;
            folderProperties.setEnName( folderProp.getStringValue("enName"));
            folderProperties.setCode( folderProp.getStringValue("code"));
            folderProperties.setLevel( folderProp.getStringValue("level"));
            folderProperties.setType( folderProp.getStringValue("type"));
            folderProperties.setProgressDuration( folderProp.getStringValue("InprogressDuration"));
            folderProperties.setInterDuration( folderProp.getStringValue("IntermediateDuration"));
            folderProperties.setDurationUnit( folderProp.getStringValue("DurationUnit"));
            folderProperties.setFinalDeter( folderProp.getStringValue("FinalDetermination"));
            folderProperties.setOpend( folderProp.getBooleanValue("isOpend"));
            folderProperties.setOwnerID(folderProp.getStringValue("ownerID"));

            StringList owners = folderProp.getStringListValue("Owners");
            String[] stringArray = (String[]) owners.toArray(new String[owners.size()]);
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(stringArray));
            folderProperties.setOwners(arrayList);

//            System.out.println(folderProperties.toString());
            folderAttributes.add(folderProperties);
        }
        System.out.println(folderAttributes);
        return folderAttributes;
    }

    public ArrayList<LeafFolderAttributes> getFilesByOwnerID (String ownerID) {
        ObjectStore objectStore = getObjectStore(getCEConnection());
        ArrayList<LeafFolderAttributes> folderAttributes = new ArrayList<>();
        SearchScope search = new SearchScope(objectStore);
        String mySQL;
        mySQL = "SELECT [This], [arName], [ClassDescription], [CmIndexingFailureCode], " +
                "[CmIsMarkedForDeletion], [CmRetentionDate], [ContainerType], [Creator], [DateCreated], " +
                "[DateLastModified], [DurationUnit], [FinalDetermination], [FolderName], [Id], " +
                "[IndexationId], [InheritParentPermissions], [InprogressDuration], [IntermediateDuration], " +
                "[IsHiddenContainer], [LastModifier], [LockOwner], [LockTimeout], [LockToken], [Name], " +
                "[Owner], [Owners], [PathName], [code], [enName], [ownerID], [level],[isOpend], " +
                "[type] FROM [leaf] WHERE [ownerID] ='" + ownerID + "' OPTIONS(TIMELIMIT 180)";

        SearchSQL sql = new SearchSQL(mySQL);
        FolderSet folders = (FolderSet) search.fetchObjects(sql, Integer.valueOf("500"), null, Boolean.TRUE);
        Iterator it1 = folders.iterator();
        while (it1.hasNext()) {
            Folder folder = (Folder) it1.next();
            Properties folderProp = folder.getProperties();
            LeafFolderAttributes folderProperties= new LeafFolderAttributes();

            folderProperties.setFolderID(String.valueOf(folderProp.getIdValue("Id")));
            folderProperties.setParentID("No data");
            folderProperties.setArName(folderProp.getStringValue("ArName")) ;
            folderProperties.setEnName( folderProp.getStringValue("enName"));
            folderProperties.setCode( folderProp.getStringValue("code"));
            folderProperties.setLevel( folderProp.getStringValue("level"));
            folderProperties.setType( folderProp.getStringValue("type"));
            folderProperties.setOpend( folderProp.getBooleanValue("isOpend"));
            folderProperties.setOwnerID(folderProp.getStringValue("ownerID"));
            folderProperties.setProgressDuration( folderProp.getStringValue("InprogressDuration"));
            folderProperties.setInterDuration( folderProp.getStringValue("IntermediateDuration"));
            folderProperties.setDurationUnit( folderProp.getStringValue("DurationUnit"));
            folderProperties.setFinalDeter( folderProp.getStringValue("FinalDetermination"));


            StringList owners = folderProp.getStringListValue("Owners");
            String[] stringArray = (String[]) owners.toArray(new String[owners.size()]);
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(stringArray));
            folderProperties.setOwners(arrayList);

//            System.out.println(folderProperties.toString());
            folderAttributes.add(folderProperties);
        }
        System.out.println(folderAttributes);
        return folderAttributes;
    }

    public void deleteFolderByID(String folderID){
        ObjectStore objectStore = getObjectStore(getCEConnection());
        Folder folder = Factory.Folder.fetchInstance(objectStore, new Id(folderID), null);
        folder.delete();
        folder.save(RefreshMode.NO_REFRESH);
    }

    public void updateFolderStatus(String folderID, boolean isOpend){
        ObjectStore objectStore = getObjectStore(getCEConnection());
        Folder folder = Factory.Folder.fetchInstance(objectStore, new Id(folderID), null);
        Properties p = folder.getProperties();
        p.putValue("isOpend", isOpend);
        folder.save(RefreshMode.NO_REFRESH);
    }

    public  String getMimeType(String fileUrl) {

        MimetypesFileTypeMap mimeTypesMap  =  new MimetypesFileTypeMap();
        String mimeType = mimeTypesMap.getContentType(fileUrl);
        return mimeType;
    }
    public void createCorrespondenceDoc(CorrespondenceAttribute correspondenceAttribute) {
        ObjectStore objectStore = getObjectStore(getCEConnection());
        Document document = null;
        //Get Folder
        Folder folder=null;
      String folderName = "/Sample";
        folder=Factory.Folder.fetchInstance(objectStore, folderName, null);

        File f = new File(correspondenceAttribute.getPath());

        //Get the File details
        String fileName = "";
        int fileSize = 0;

// Create Document

        String docClass = "Correspondence";
        try {
            FileInputStream file = new FileInputStream(f);
            Document doc = Factory.Document.createInstance(objectStore, docClass);
            if (file != null && fileSize > 0) {
                ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
                ContentElementList contentElementList = Factory.ContentElement.createList();

                contentTransfer.setCaptureSource(file);
                contentElementList.add(contentTransfer);
                doc.set_ContentElements(contentElementList);
                contentTransfer.set_RetrievalName(fileName);
                doc.set_MimeType(getMimeType(fileName));

            }


//Check-in the doc
            doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
//Get and put the doc properties
            String documentName = "";
            Properties p = doc.getProperties();
            p.putValue("CorrespondenceID", correspondenceAttribute.getCorrespondenceID());
            p.putValue("Subject", correspondenceAttribute.getSubject());
            StringList senders = Factory.StringList.createList();
            boolean b = senders.addAll(correspondenceAttribute.getSenders());
            p.putValue("Senders", senders);

            StringList recievers = Factory.StringList.createList();
            boolean b1 = recievers.addAll(correspondenceAttribute.getRecievers());
            p.putValue("Recievers", recievers);

            StringList cc = Factory.StringList.createList();
            boolean b2 = cc.addAll(correspondenceAttribute.getCc());
            p.putValue("CC", cc);

            StringList bcc = Factory.StringList.createList();
            boolean b3 = bcc.addAll(correspondenceAttribute.getBcc());
            p.putValue("BCC", bcc);
            doc.save(RefreshMode.REFRESH);

//Stores above content to the folder
            ReferentialContainmentRelationship rc = folder.file(doc,
                    AutoUniqueName.AUTO_UNIQUE,
                    documentName,
                    DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
            rc.save(RefreshMode.REFRESH);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
