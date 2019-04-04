package com.efhems.newinsideproject.data.local.entities;

public class ProjectTask {

    private Task task;
    private String taskId;
    private String projectName;

    public ProjectTask(Task task, String taskId, String projectName) {
        this.task = task;
        this.taskId = taskId;
        this.projectName = projectName;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
