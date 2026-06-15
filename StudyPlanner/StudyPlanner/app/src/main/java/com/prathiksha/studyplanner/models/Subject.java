package com.prathiksha.studyplanner.models;

public class Subject {
    private String subjectId;
    private String userId;
    private String subjectName;

    public Subject() {}

    public Subject(String subjectId, String userId, String subjectName) {
        this.subjectId = subjectId;
        this.userId = userId;
        this.subjectName = subjectName;
    }

    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
}