package com.efhems.newinsideproject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.efhems.newinsideproject.data.local.daos.ProjectTaskDao;
import com.efhems.newinsideproject.data.local.entities.Project;
import com.efhems.newinsideproject.data.local.entities.Task;

import java.util.List;

public class ProjectReminderRepo {

    private ProjectTaskDao projectTaskDao;

    public ProjectReminderRepo(ProjectTaskDao projectTaskDao) {
        this.projectTaskDao = projectTaskDao;
    }

    public LiveData<List<Project>> getAllProject() {

        // Returns a LiveData object directly from the database.
        return projectTaskDao.loadAllProject();
    }

    public LiveData<List<Task>> getTasks(final int projectId){

        return projectTaskDao.loadTasksById(projectId);
    }
}
