package com.prathiksha.studyplanner.models;

public class Task {
    private String taskId;
    private String subjectId;
    private String title;
    private String deadline;
    private boolean completed;

    public Task() {}

    public Task(String taskId, String subjectId, String title, String deadline) {
        this.taskId = taskId;
        this.subjectId = subjectId;
        this.title = title;
        this.deadline = deadline;
        this.completed = false;
    }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}