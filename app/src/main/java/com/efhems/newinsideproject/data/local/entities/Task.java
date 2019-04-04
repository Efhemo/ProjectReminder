package com.efhems.newinsideproject.data.local.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class Task implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long projectId;
    private long notification;
    private String date;
    private String title;
    private String description;
    private int priority;

    public long getNotification() {
        return notification;
    }

    private String record;

    protected Task(Parcel in) {
        id = in.readInt();
        projectId = in.readLong();
        notification = in.readLong();
        date = in.readString();
        title = in.readString();
        description = in.readString();
        priority = in.readInt();
        record = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(projectId);
        dest.writeLong(notification);
        dest.writeString(date);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(priority);
        dest.writeString(record);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    @Ignore
    public Task() {
    }

    public Task(int id, long projectId, long notification, String date, String title, String description, int priority, String record) {
        this.id = id;
        this.projectId = projectId;
        this.notification = notification;
        this.date = date;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.record = record;
    }

    @Ignore
    public Task(long projectId, long notification, String date, String title, String description, int priority, String record) {
        this.projectId = projectId;
        this.notification = notification;
        this.date = date;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.record = record;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*public long isNotification() {
        return notification;
    }*/

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public String getRecord() {
        return record;
    }

    public void setNotification(long notification) {
        this.notification = notification;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
