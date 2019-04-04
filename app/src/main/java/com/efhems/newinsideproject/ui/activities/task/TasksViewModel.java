package com.efhems.newinsideproject.ui.activities.task;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.efhems.newinsideproject.AppExecutors;
import com.efhems.newinsideproject.ProjectReminderRepo;
import com.efhems.newinsideproject.data.ProjectReminderDatabase;
import com.efhems.newinsideproject.data.local.daos.ProjectTaskDao;
import com.efhems.newinsideproject.data.local.entities.Task;

import java.util.List;

public class TasksViewModel extends AndroidViewModel {

    private LiveData<List<Task>> tasks;

    private ProjectReminderRepo projectReminderRepo;

    public TasksViewModel(@NonNull Application application) {
        super(application);

        ProjectTaskDao projectTaskDao = ProjectReminderDatabase.getInstance(application).projectTaskDao();
        projectReminderRepo = new ProjectReminderRepo(projectTaskDao);
    }

    public void setProjectId(int projectId){

        if (this.tasks != null) {
            // ViewModel is created on a per-Fragment basis, so the projectId
            // doesn't change.
            return;
        }

        tasks = projectReminderRepo.getTasks(projectId);
    }

    public LiveData<List<Task>> getTasks(){

        return tasks;
    }


}
