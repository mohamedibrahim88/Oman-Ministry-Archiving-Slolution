package com.example.gateway.enities;

public class CrsDto {
    String referenceNumber;
    String subject;
    boolean isOpened;

    public CrsDto() {
    }

    public CrsDto(String referenceNumber, String subject, boolean isOpened) {
        this.referenceNumber = referenceNumber;
        this.subject = subject;
        this.isOpened = isOpened;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    @Override
    public String toString() {
        return "CrsDto{" +
                "referenceNumber='" + referenceNumber + '\'' +
                ", subject='" + subject + '\'' +
                ", isOpened=" + isOpened +
                '}';
    }
}
