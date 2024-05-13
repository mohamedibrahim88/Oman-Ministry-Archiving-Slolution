package com.example.gateway.enities;

import java.util.Date;

public class RetentionEndDates {
    Date progressEndDate;
    Date intermediateEndDate;

    public RetentionEndDates() {
    }

    public RetentionEndDates(Date progressEndDate, Date intermediateEndDate) {
        this.progressEndDate = progressEndDate;
        this.intermediateEndDate = intermediateEndDate;
    }

    public Date getProgressEndDate() {
        return progressEndDate;
    }

    public void setProgressEndDate(Date progressEndDate) {
        this.progressEndDate = progressEndDate;
    }

    public Date getIntermediateEndDate() {
        return intermediateEndDate;
    }

    public void setIntermediateEndDate(Date intermediateEndDate) {
        this.intermediateEndDate = intermediateEndDate;
    }

    @Override
    public String toString() {
        return "RetentionEndDates{" +
                "progressEndDate=" + progressEndDate +
                ", intermediateEndDate=" + intermediateEndDate +
                '}';
    }
}
