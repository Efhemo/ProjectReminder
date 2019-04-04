package com.efhems.newinsideproject.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.efhems.newinsideproject.data.local.daos.ProjectTaskDao;
import com.efhems.newinsideproject.data.local.entities.Project;
import com.efhems.newinsideproject.data.local.entities.Task;
import com.efhems.newinsideproject.utilities.ListConverter;

// List of the entry classes and associated TypeConverters
@Database(entities = {Project.class, Task.class}, version = 1, exportSchema = false)
@TypeConverters(ListConverter.class)
public abstract class ProjectReminderDatabase extends RoomDatabase {

    private static final String LOG_TAG = ProjectReminderDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "projectreminder";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static ProjectReminderDatabase sInstance;

    public static ProjectReminderDatabase getInstance(Context context) {
        // Log.d(LOG_TAG, "Getting the database");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        ProjectReminderDatabase.class, ProjectReminderDatabase.DATABASE_NAME)
//                        .addCallback(sRoomDatabaseCallback)
                        .build();
                // Log.d(LOG_TAG, "Made new database");
            }
        }
        return sInstance;
    }

    // The associated DAOs for the database
    public abstract ProjectTaskDao projectTaskDao();

}