
package com.efhems.newinsideproject.ui.activities.projectList;

import android.animation.Animator;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.efhems.newinsideproject.AppExecutors;
import com.efhems.newinsideproject.data.ProjectReminderDatabase;
import com.efhems.newinsideproject.data.local.entities.Project;
import com.efhems.newinsideproject.ui.activities.task.TaskActivity;
import com.efhems.newinsideproject.ui.adapter.MyProjectRecyclerAdapter;
import com.efhems.newinsideproject.R;
import com.efhems.newinsideproject.ui.widget.UpdateService;

import java.util.ArrayList;
import java.util.List;

public class MyProjectActivity extends AppCompatActivity implements MyProjectRecyclerAdapter.OnProjectClickListener {

    private static final String TAG = MyProjectActivity.class.getSimpleName();
    public static final String CURRENT_PROJECT = "PROJECT";
    RecyclerView projectRecyclerView;


    private MyProjectRecyclerAdapter myProjectRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_project);
        setTitle(R.string.my_project);
        initProjectRecyclerView();

        ProjectLiveViewModel projectLiveViewModel = ViewModelProviders.of(this).get(ProjectLiveViewModel.class);
        projectLiveViewModel.getAllProject().observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(@Nullable List<Project> projects) {



                if (projects != null) {
                    myProjectRecyclerAdapter.setProject(projects);

                    ArrayList<Project> arrayList = new ArrayList<Project>(projects);
                    UpdateService.startService(getApplicationContext(), arrayList);
                    Log.d(TAG, "onChanged: projects size is "+ projects.size());
                }

            }
        });

        ConstraintLayout rootview =  findViewById(R.id.projects_list_root);
        rootview.setBackgroundColor(getResources().getColor(R.color.textColorPrimaryInverse));
        rootview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);

                //LEFT TOP TO BOTTOM RIGHT ANIMATION
                int cx1 = 0;
                int cy1 = 0;
                // get the hypothenuse so the radius is from one corner to the other
                int radius1 = (int) Math.hypot(right, bottom);
                Animator reveal1 = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    reveal1 = ViewAnimationUtils.createCircularReveal(v, cx1, cy1, 0, radius1);
                }
                reveal1.setInterpolator(new DecelerateInterpolator(2f));
                reveal1.setDuration(2000);
                reveal1.start();
            }
        });

        final ProjectReminderDatabase mDb = ProjectReminderDatabase.getInstance(this);

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Project> projects = myProjectRecyclerAdapter.getProjectList();
                        int deleteProjectId = mDb.projectTaskDao().deleteProject(projects.get(position));
                        mDb.projectTaskDao().deleteAllTaskByProjectId(deleteProjectId);
                    }
                });
            }


        }).attachToRecyclerView(projectRecyclerView);
    }

    private void initProjectRecyclerView(){
        projectRecyclerView = findViewById(R.id.projectRecyclerView);
        myProjectRecyclerAdapter = new MyProjectRecyclerAdapter(this, this);
        projectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        projectRecyclerView.setAdapter(myProjectRecyclerAdapter);
        projectRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onProjectClick(Project project) {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(CURRENT_PROJECT, project);
        startActivity(intent);
    }
}
