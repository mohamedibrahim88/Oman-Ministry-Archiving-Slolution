package com.example.gateway.enities;

import java.util.ArrayList;

public class CrsClassifcation {

    CrsClassificationAttribute crsClassification = new CrsClassificationAttribute();

    ArrayList<CrsClassificationAttribute> crsClassificationProperties = new ArrayList<>();

    public CrsClassifcation() {
    }

    public CrsClassifcation(CrsClassificationAttribute crsClassification, ArrayList<CrsClassificationAttribute> crsClassificationProperties) {
        this.crsClassification = crsClassification;
        this.crsClassificationProperties = crsClassificationProperties;
    }

    public CrsClassificationAttribute getCrsClassification() {
        return crsClassification;
    }

    public void setCrsClassification(CrsClassificationAttribute crsClassification) {
        this.crsClassification = crsClassification;
    }

    public ArrayList<CrsClassificationAttribute> getCrsClassificationProperties() {
        return crsClassificationProperties;
    }

    public void setCrsClassificationProperties(ArrayList<CrsClassificationAttribute> crsClassificationProperties) {
        this.crsClassificationProperties = crsClassificationProperties;
    }

    @Override
    public String toString() {
        return "CrsClassifcation{" +
                "crsClassification=" + crsClassification +
                ", crsClassificationProperties=" + crsClassificationProperties +
                '}';
    }
//    String displayName;
//    String sympolicName;
//

//
//    public CrsClassifcation(String displayName, String sympolicName) {
//        this.displayName = displayName;
//        this.sympolicName = sympolicName;
//    }
//
//    public String getDisplayName() {
//        return displayName;
//    }
//
//    public void setDisplayName(String displayName) {
//        this.displayName = displayName;
//    }
//
//    public String getSympolicName() {
//        return sympolicName;
//    }
//
//    public void setSympolicName(String sympolicName) {
//        this.sympolicName = sympolicName;
//    }
//
//    @Override
//    public String toString() {
//        return "CrsClassifcation{" +
//                "displayName='" + displayName + '\'' +
//                ", sympolicName='" + sympolicName + '\'' +
//                '}';
//    }
}
