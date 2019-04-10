package com.efhems.newinsideproject.ui.activities.task;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.efhems.newinsideproject.AppExecutors;
import com.efhems.newinsideproject.R;
import com.efhems.newinsideproject.data.ProjectReminderDatabase;
import com.efhems.newinsideproject.data.local.entities.Project;
import com.efhems.newinsideproject.data.local.entities.Task;
import com.efhems.newinsideproject.ui.activities.addTask.AddTaskActivity;
import com.efhems.newinsideproject.ui.adapter.TasksReminderRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.efhems.newinsideproject.ui.activities.projectList.MyProjectActivity.CURRENT_PROJECT;

public class TaskActivity extends AppCompatActivity implements TasksReminderRecyclerAdapter.OnTaskClickListener {
    private static final String TAG = TaskActivity.class.getSimpleName();
    public static final String CURRENT_TASK = "current task";
    private RecyclerView taskRecyclerView;
    private TasksReminderRecyclerAdapter tasksReminderRecyclerAdapter;
    private ProgressBar progressBar;
    private FloatingActionButton addFab;
    private Project currentProject;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDBReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_reminder);


        final Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        // init View
        initLayout();


        Log.d(TAG, "onCreate: you pressed back");
        if (intent != null && intent.hasExtra(CURRENT_PROJECT)) {
            currentProject = intent.getParcelableExtra(CURRENT_PROJECT);

            getSupportActionBar().setTitle(currentProject.getNameOfProject());

            TasksViewModel tasksViewModel = ViewModelProviders.of(this).get(TasksViewModel.class);

            tasksViewModel.setProjectId(currentProject.getId());
            tasksViewModel.getTasks().observe(this, new Observer<List<Task>>() {
                @Override
                public void onChanged(@Nullable List<Task> tasks) {

                    Log.d(TAG, "onCreate: you go front");

                    if (tasks != null) {
                        progressBar.setVisibility(View.INVISIBLE);
                        tasksReminderRecyclerAdapter.setTasks(tasks);
                    }
                }
            });
            Log.d(TAG, "onCreate: value is ");


        }

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentProject != null) {

                    Intent addTaskIntent = new Intent(TaskActivity.this, AddTaskActivity.class);
                    addTaskIntent.putExtra(CURRENT_PROJECT, currentProject);
                    startActivity(addTaskIntent);
                }
            }
        });


        firebaseDatabase = FirebaseDatabase.getInstance();
        mDBReference = firebaseDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void initLayout() {

        tasksReminderRecyclerAdapter = new TasksReminderRecyclerAdapter(this, this);
        addFab = findViewById(R.id.add_fab);
        progressBar = findViewById(R.id.task_progress);
        taskRecyclerView = findViewById(R.id.task_recycler);
        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        taskRecyclerView.addItemDecoration(itemDecoration);
        taskRecyclerView.setAdapter(tasksReminderRecyclerAdapter);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.close_on_error, Toast.LENGTH_SHORT).show();
    }

    private void writeTaskToFirebase(Task task) {

        if (user.getEmail() == null) {
            Toast.makeText(this, getString(R.string.you_need_to_sign_in), Toast.LENGTH_SHORT).show();
        } else {
            mDBReference.child(user.getDisplayName() + " " + user.getUid()).child(currentProject.getNameOfProject())
                    .child(task.getId() + "").setValue(task);
            Toast.makeText(this, getString(R.string.save_btn), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onTaskClick(Task task, TextView tvPriority) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra(CURRENT_TASK, task);
        intent.putExtra(CURRENT_PROJECT, currentProject);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(tvPriority, "PriorityTransition");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(TaskActivity.this, pairs);
            startActivity(intent, options.toBundle());
        } else {

            startActivity(intent);
        }
    }

    @Override
    public void onMoreVertClick(final Task task, ImageView moreVert) {
        PopupMenu popupMenu = new PopupMenu(moreVert.getContext(), moreVert);
        popupMenu.inflate(R.menu.popup_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete:
                        final ProjectReminderDatabase mDb = ProjectReminderDatabase.getInstance(getApplicationContext());
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {

                                mDb.projectTaskDao().deleteTaskByid(task.getId());
                            }
                        });
                        break;
                    case R.id.push_online:
                        if (user != null) {
                            writeTaskToFirebase(task);
                        }else{
                            Toast.makeText(TaskActivity.this, R.string.you_need_to_sign_in, Toast.LENGTH_SHORT).show();
                        }

                        break;
                }
                return true;
            }
        });

        popupMenu.show();
    }
}
