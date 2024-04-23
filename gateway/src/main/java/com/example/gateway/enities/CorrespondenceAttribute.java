package com.example.gateway.enities;

import java.util.ArrayList;

public class CorrespondenceAttribute {

    String folderID;
    String userID;
    String correspondenceID;
    String subject;
    String path;
    String docTitle;
    ArrayList<String> senders;
    ArrayList<String> recievers;
    ArrayList<String> cc;
    ArrayList<String> bcc;

    ArrayList<AttachmentsAttributes> attachmentsAttributes;

    public CorrespondenceAttribute() {
    }

    public CorrespondenceAttribute(String folderID, String userID, String correspondenceID, String subject, String path, String docTitle, ArrayList<String> senders, ArrayList<String> recievers, ArrayList<String> cc, ArrayList<String> bcc, ArrayList<AttachmentsAttributes> attachmentsAttributes) {
        this.folderID = folderID;
        this.userID = userID;
        this.correspondenceID = correspondenceID;
        this.subject = subject;
        this.path = path;
        this.docTitle = docTitle;
        this.senders = senders;
        this.recievers = recievers;
        this.cc = cc;
        this.bcc = bcc;
        this.attachmentsAttributes = attachmentsAttributes;
    }

    public String getFolderID() {
        return folderID;
    }

    public void setFolderID(String folderID) {
        this.folderID = folderID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCorrespondenceID() {
        return correspondenceID;
    }

    public void setCorrespondenceID(String correspondenceID) {
        this.correspondenceID = correspondenceID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public ArrayList<String> getSenders() {
        return senders;
    }

    public void setSenders(ArrayList<String> senders) {
        this.senders = senders;
    }

    public ArrayList<String> getRecievers() {
        return recievers;
    }

    public void setRecievers(ArrayList<String> recievers) {
        this.recievers = recievers;
    }

    public ArrayList<String> getCc() {
        return cc;
    }

    public void setCc(ArrayList<String> cc) {
        this.cc = cc;
    }

    public ArrayList<String> getBcc() {
        return bcc;
    }

    public void setBcc(ArrayList<String> bcc) {
        this.bcc = bcc;
    }

    public ArrayList<AttachmentsAttributes> getAttachmentsAttributes() {
        return attachmentsAttributes;
    }

    public void setAttachmentsAttributes(ArrayList<AttachmentsAttributes> attachmentsAttributes) {
        this.attachmentsAttributes = attachmentsAttributes;
    }

    @Override
    public String toString() {
        return "CorrespondenceAttribute{" +
                "folderID='" + folderID + '\'' +
                ", userID='" + userID + '\'' +
                ", correspondenceID='" + correspondenceID + '\'' +
                ", subject='" + subject + '\'' +
                ", path='" + path + '\'' +
                ", docTitle='" + docTitle + '\'' +
                ", senders=" + senders +
                ", recievers=" + recievers +
                ", cc=" + cc +
                ", bcc=" + bcc +
                ", attachmentsAttributes=" + attachmentsAttributes +
                '}';
    }
}
