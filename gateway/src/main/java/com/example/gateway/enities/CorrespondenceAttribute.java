package com.example.gateway.enities;

import java.util.ArrayList;
import java.util.HashMap;

public class CorrespondenceAttribute {
    String userID;
    String correspondenceID;
    String subject;
    String path;
    String docTitle;
    ArrayList<String> senders;
    ArrayList<String> recievers;
    ArrayList<String> cc;
    ArrayList<String> bcc;

    String classification;
    HashMap<String,Object> prop = new HashMap<String,Object>();
    ArrayList<AttachmentsAttributes> attachmentsAttributes;

    public CorrespondenceAttribute() {
    }

    public CorrespondenceAttribute( String userID, String correspondenceID, String subject, String path, String docTitle, ArrayList<String> senders, ArrayList<String> recievers, ArrayList<String> cc, ArrayList<String> bcc, String classification, HashMap<String, Object> prop, ArrayList<AttachmentsAttributes> attachmentsAttributes) {
        this.userID = userID;
        this.correspondenceID = correspondenceID;
        this.subject = subject;
        this.path = path;
        this.docTitle = docTitle;
        this.senders = senders;
        this.recievers = recievers;
        this.cc = cc;
        this.bcc = bcc;
        this.classification = classification;
        this.prop = prop;
        this.attachmentsAttributes = attachmentsAttributes;
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

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public HashMap<String, Object> getProp() {
        return prop;
    }

    public void setProp(HashMap<String, Object> prop) {
        this.prop = prop;
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
                ", userID='" + userID + '\'' +
                ", correspondenceID='" + correspondenceID + '\'' +
                ", subject='" + subject + '\'' +
                ", path='" + path + '\'' +
                ", docTitle='" + docTitle + '\'' +
                ", senders=" + senders +
                ", recievers=" + recievers +
                ", cc=" + cc +
                ", bcc=" + bcc +
                ", classification='" + classification + '\'' +
                ", prop=" + prop +
                ", attachmentsAttributes=" + attachmentsAttributes +
                '}';
    }
}
