package com.example.gateway.constants;

public enum RetentionCases {
    ArchiveFolder(0),
    CRSFolder(1),

    Document(2)
    ;

    private int cases;

    RetentionCases(int cases)
    {
        this.cases = cases;
    }

    public int getCases() {
        return cases;
    }
}
