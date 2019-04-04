package com.efhems.newinsideproject.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.efhems.newinsideproject.R;
import com.efhems.newinsideproject.data.local.entities.ProjectTask;
import com.efhems.newinsideproject.data.local.entities.Task;
import com.efhems.newinsideproject.ui.adapter.ProjectTaskRCAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CloudStorageActivity extends AppCompatActivity {

    private static final String LOG_TAG = CloudStorageActivity.class.getSimpleName();
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_storage);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        RecyclerView rcCloud = findViewById(R.id.rc_cloud);

        final ProjectTaskRCAdapter projectTaskRCAdapter = new ProjectTaskRCAdapter(this);
        rcCloud.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcCloud.setHasFixedSize(true);
        rcCloud.setAdapter(projectTaskRCAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                List<ProjectTask> projectTasks = new ArrayList<>();
                DataSnapshot dataSnapshot1 = dataSnapshot.child(user.getDisplayName() +" "+user.getUid());
                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                    String projectName = dataSnapshot2.getKey(); //get all project name
                    for (DataSnapshot dataSnapshot3: dataSnapshot2.getChildren()) { // task id result
                        //get task here
                        Log.d(LOG_TAG, "onDataChange: "+projectName);
                        String id = dataSnapshot3.getKey();

                        Task task = dataSnapshot3.getValue(Task.class);
                        ProjectTask projectTask = new ProjectTask(task, id, projectName);

                        Log.d(LOG_TAG, "onDataChange: "+task.getTitle() +" "+task.getDate()
                                +" "+task.getDescription()+" "+task.getPriority());

                        projectTasks.add(projectTask);
                    }
                }

                projectTaskRCAdapter.setProject(projectTasks);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(LOG_TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);
    }



}
