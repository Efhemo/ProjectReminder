package com.efhems.newinsideproject.data.local.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

@Entity
public class Project implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nameOfProject;
    private String descOfProject;

    @Ignore
    public Project() {
    }

    public Project(int id, String nameOfProject, String descOfProject) {
        this.id = id;
        this.nameOfProject = nameOfProject;
        this.descOfProject = descOfProject;
    }

    @Ignore
    public Project(String nameOfProject, String descOfProject) {
        this.nameOfProject = nameOfProject;
        this.descOfProject = descOfProject;
    }

    protected Project(Parcel in) {
        id = in.readInt();
        nameOfProject = in.readString();
        descOfProject = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nameOfProject);
        dest.writeString(descOfProject);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameOfProject() {
        return nameOfProject;
    }

    public void setNameOfProject(String nameOfProject) {
        this.nameOfProject = nameOfProject;
    }

    public String getDescOfProject() {
        return descOfProject;
    }

    public void setDescOfProject(String descOfProject) {
        this.descOfProject = descOfProject;
    }

}
