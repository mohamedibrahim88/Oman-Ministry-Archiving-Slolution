package com.example.gateway.schedular;

import com.example.gateway.client.FileNet;
import com.example.gateway.constants.ClassificationFolder;
import com.example.gateway.constants.FinalDetermination;
import com.example.gateway.constants.RetentionStatus;
import com.example.gateway.constants.UserArchivingFolder;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.Properties;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;

@Component
public class scheduler {
    @Autowired
    FileNet fileNet;
    @Scheduled(cron = "${cron.expression}")
    public void schedulerJob(){
        ObjectStore objectStore = fileNet.getObjectStore(fileNet.getCEConnection());
        SearchScope search = new SearchScope(objectStore);
        changeStatus(objectStore,search);
        deleteExpiredFolder(objectStore,search);
    }

private void changeStatus (ObjectStore objectStore,SearchScope search){

    LocalDateTime minDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
    LocalDateTime maxDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).plusDays(1);

    System.out.println(minDate);
    String mySQL = "SELECT [This],* FROM [UserArchivingFolder] " +
            "WHERE [progressEndDate] >= "+minDate+" AND [progressEndDate] < "+maxDate+" " +
            "AND [retentionStatus] = '"+ RetentionStatus.ACTIVE.toString()+"' " +
            "OPTIONS(TIMELIMIT 180)";

    SearchSQL sql = new SearchSQL(mySQL);

    FolderSet folders = (FolderSet) search.fetchObjects(sql, Integer.valueOf("500"), null, Boolean.TRUE);
    Iterator it1 = folders.iterator();
    while (it1.hasNext()) {
        Folder folder = (Folder) it1.next();
        Properties p = folder.getProperties();
        p.putValue(UserArchivingFolder.retentionStatus.toString(), RetentionStatus.INTERMEDIATE.toString());
        folder.save(RefreshMode.REFRESH);
        FolderSet crsFolders = folder.get_SubFolders();
        Iterator crsFolderIt = crsFolders.iterator();

        while (crsFolderIt.hasNext())
        {
            Folder crsFolder = (Folder) crsFolderIt.next();
            Properties crsP = crsFolder.getProperties();
            crsP.putValue(UserArchivingFolder.retentionStatus.toString(), RetentionStatus.INTERMEDIATE.toString());
            crsFolder.save(RefreshMode.REFRESH);
        }
    }


}

private void deleteExpiredFolder(ObjectStore objectStore, SearchScope search){
    LocalDateTime minDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
    LocalDateTime maxDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).plusDays(1);

    System.out.println(minDate);
    String mySQL = "SELECT [This],* FROM [UserArchivingFolder] " +
            "WHERE [CmRetentionDate] >= "+minDate+" AND [CmRetentionDate] < "+maxDate+" " +
            "AND [finalDetermination] = '"+ FinalDetermination.DELETE.toString()+"' " +
            "OPTIONS(TIMELIMIT 180)";

    SearchSQL sql = new SearchSQL(mySQL);

    FolderSet folders = (FolderSet) search.fetchObjects(sql, Integer.valueOf("500"), null, Boolean.TRUE);
    Iterator it1 = folders.iterator();
    while (it1.hasNext()) {
        Folder folder = (Folder) it1.next();
        String folderID = folder.get_Id().toString();
        fileNet.deleteFolderByID(folderID);
    }
    }

}

