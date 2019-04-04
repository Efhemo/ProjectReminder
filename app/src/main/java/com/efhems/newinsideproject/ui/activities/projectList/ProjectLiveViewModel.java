package com.efhems.newinsideproject.ui.activities.projectList;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.efhems.newinsideproject.ProjectReminderRepo;
import com.efhems.newinsideproject.data.ProjectReminderDatabase;
import com.efhems.newinsideproject.data.local.daos.ProjectTaskDao;
import com.efhems.newinsideproject.data.local.entities.Project;

import java.util.List;

public class ProjectLiveViewModel extends AndroidViewModel {


    private final LiveData<List<Project>> projectLiveData;

    public ProjectLiveViewModel(@NonNull Application application) {
        super(application);
        ProjectTaskDao projectTaskDao = ProjectReminderDatabase.getInstance(application).projectTaskDao();
        projectLiveData = new ProjectReminderRepo(projectTaskDao).getAllProject();
    }

    public LiveData<List<Project>> getAllProject() {
        return projectLiveData;

    }
}
