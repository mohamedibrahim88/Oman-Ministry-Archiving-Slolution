package com.example.gateway.enities;

import java.util.HashMap;

public class AttachmentsAttributes {
    String ID;
    String classification;
    String path;
    HashMap<String,String> prop = new HashMap<String,String>();

    public AttachmentsAttributes() {
    }

    public AttachmentsAttributes(String ID, String classification, String path, HashMap<String, String> prop) {
        this.ID = ID;
        this.classification = classification;
        this.path = path;
        this.prop = prop;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HashMap<String, String> getProp() {
        return prop;
    }

    public void setProp(HashMap<String, String> prop) {
        this.prop = prop;
    }

    @Override
    public String toString() {
        return "AttachmentsAttributes{" +
                "ID='" + ID + '\'' +
                ", classification='" + classification + '\'' +
                ", path='" + path + '\'' +
                ", prop=" + prop +
                '}';
    }
}
