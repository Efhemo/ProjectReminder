package com.efhems.newinsideproject.utilities;

import android.arch.persistence.room.TypeConverter;

import com.efhems.newinsideproject.data.local.entities.Project;
import com.efhems.newinsideproject.data.local.entities.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class ListConverter implements Serializable {

    @TypeConverter
    public String fromList (List<Task> taskList){
        if(taskList == null){
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Task>>(){}.getType();
        return gson.toJson(taskList, type);
    }

    @TypeConverter
    public List<Task> toList (String listTask){
        if(listTask == null){
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Task>>(){}.getType();
        return gson.fromJson(listTask, type);
    }
}