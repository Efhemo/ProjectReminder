package com.efhems.newinsideproject.data.local.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.efhems.newinsideproject.data.local.entities.Project;
import com.efhems.newinsideproject.data.local.entities.Task;

import java.util.List;

@Dao
public interface  ProjectTaskDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProject(Project project);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTaskByProjectId(Task task);

    @Query("Delete FROM task WHERE id=:id")
    int deleteTaskByid(int id);


    @Query("SELECT * FROM Task WHERE projectId = :projectId")
    List<Task> loadTasksByProjectId(long projectId);

    @Query("SELECT * FROM project")
    LiveData<List<Project>> loadAllProject();

    @Query("SELECT * FROM task WHERE projectId =:projectId ORDER BY priority ASC")
    LiveData<List<Task>> loadTasksById(long projectId);

    @Delete
    int deleteProject(Project project);

    @Query("Delete FROM task WHERE projectId=:projectId")
    int deleteAllTaskByProjectId(int projectId);


}
