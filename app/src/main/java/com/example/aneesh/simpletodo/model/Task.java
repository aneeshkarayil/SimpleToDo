package com.example.aneesh.simpletodo.model;

import java.sql.Date;
import java.util.UUID;

/**
 * Created by Aneesh on 12/27/2016.
 */

public class Task {

    private String description;
    private UUID taskId;
    private UUID parentId;
    private boolean isDone;
    private Date startDate;

    public Task(String description)
    {
        this(description, null);
    }

    public Task(String description, UUID parentId)
    {
        this.description = description;
        this.taskId = UUID.randomUUID();
        this.parentId = parentId;
        this.isDone = false;
        this.startDate = new Date(System.currentTimeMillis());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getParentId() {
        return parentId;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public Date getStartDate() {
        return startDate;
    }

    public UUID getTaskId() {
        return taskId;
    }
}
