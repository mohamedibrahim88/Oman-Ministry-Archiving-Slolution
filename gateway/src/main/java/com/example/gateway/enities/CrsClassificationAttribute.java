package com.example.gateway.enities;

public class CrsClassificationAttribute {
    String displayName;
    String sympolicName;

    public CrsClassificationAttribute() {
    }

    public CrsClassificationAttribute(String displayName, String sympolicName) {
        this.displayName = displayName;
        this.sympolicName = sympolicName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSympolicName() {
        return sympolicName;
    }

    public void setSympolicName(String sympolicName) {
        this.sympolicName = sympolicName;
    }

    @Override
    public String toString() {
        return "CrsClassificationAttribute{" +
                "displayName='" + displayName + '\'' +
                ", sympolicName='" + sympolicName + '\'' +
                '}';
    }
}
