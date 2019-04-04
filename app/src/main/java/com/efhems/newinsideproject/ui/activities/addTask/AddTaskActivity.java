package com.efhems.newinsideproject.ui.activities.addTask;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.efhems.newinsideproject.AppExecutors;
import com.efhems.newinsideproject.R;
import com.efhems.newinsideproject.data.ProjectReminderDatabase;
import com.efhems.newinsideproject.data.local.entities.Project;
import com.efhems.newinsideproject.data.local.entities.Task;
import com.efhems.newinsideproject.ui.notification.AlertReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.efhems.newinsideproject.ui.activities.main.SettingsDialog.RB_VALUE;
import static com.efhems.newinsideproject.ui.activities.projectList.MyProjectActivity.CURRENT_PROJECT;
import static com.efhems.newinsideproject.ui.activities.task.TaskActivity.CURRENT_TASK;
import static com.efhems.newinsideproject.ui.notification.AlertReceiver.NOTIFICATION_BUILT;
import static com.efhems.newinsideproject.ui.notification.AlertReceiver.NOTIFICATION_ID;
import static com.efhems.newinsideproject.ui.notification.NotificationChan.CHANNEL_ID;

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnLongClickListener, View.OnTouchListener {

    private static final String TAG = AddTaskActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSION_CODES = 1;
    private TextView projectName;
    private TextView projectDesc;
    private TextView dateTime;
    private AppCompatEditText taskTitle;
    private AppCompatEditText taskDesc;
    private ImageButton micRecorder;
    private TextView priority1;
    private TextView priority2;
    private TextView priority3;
    private Button saveBtn;
    private Button deleteBtn;

    private long projectRowId;
    private TaskRecorder taskRecorder;

    /* private TextView tv_date_time;*/
    private int year, month, day, hour, minute;

    // Member variable for the Database
    private ProjectReminderDatabase mDb;
    private ImageView imgNotification;
    private int priority1Value = -1;
    private ObjectAnimator rotateAnimation;
    private AnimatorSet animatorSet;
    private ConstraintLayout addTaskContainer;
    private Boolean isUpdating = false;
    private int taskId;
    private Project project;
    private Task task;
    private long timeInMilli = -1;
    private String dateTimeText = "";

    private boolean isSButtonLongPressed = false;

    private ImageButton playBtn;
    private Animation fadeIn;
    private String filePath1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        mDb = ProjectReminderDatabase.getInstance(getApplicationContext());
        initLayout();
        askUserForAppsPermission();


        ActionBar actionBar = getSupportActionBar();
        if (intent != null && intent.hasExtra(CURRENT_PROJECT)) {
            project = getIntent().getParcelableExtra(CURRENT_PROJECT);
            actionBar.setTitle(R.string.add_task);

            projectRowId = project.getId();
            projectName.setText(project.getNameOfProject());
            projectDesc.setText(project.getDescOfProject());

        }

        //you can update your task with this information
        if (intent != null && intent.hasExtra(CURRENT_TASK)) {
            task = getIntent().getParcelableExtra(CURRENT_TASK);

            isUpdating = true;
            taskId = task.getId();
            saveBtn.setText(R.string.update);
            actionBar.setTitle(R.string.update_task);
            projectRowId = task.getProjectId();
            taskTitle.setText(task.getTitle());
            taskDesc.setText(task.getDescription());
            priority1Value = task.getPriority();
            timeInMilli = task.getNotification();
            dateTimeText = task.getDate();
            filePath1 = task.getRecord();
            if(!dateTimeText.isEmpty()){
                dateTime.setText(dateTimeText);
                imgNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications_fill));
                Log.d(TAG, "onCreate: value of date "+dateTimeText);
            }

            if(filePath1 != null){
                if(!filePath1.isEmpty()){
                    playBtn.setVisibility(View.VISIBLE);
                }
            }

        }

        //use the current time as the default value for the picker
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        imgNotification.setOnClickListener(this);
        dateTime.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        taskRecorder = new TaskRecorder(this);

        priority1.setOnClickListener(this);
        priority2.setOnClickListener(this);
        priority3.setOnClickListener(this);
        playBtn.setOnClickListener(this);

        if (priority1Value == 1) {
            priority1.setBackgroundResource(R.drawable.red_fill_selected_bg);
            priority1.setTextAppearance(this, R.style.selected_priority_red);
        } else if (priority1Value == 2) {
            priority2.setBackgroundResource(R.drawable.green_fill_selected_bg);
            priority2.setTextAppearance(this, R.style.selected_priority_green);
        } else if (priority1Value == 3) {
            priority3.setBackgroundResource(R.drawable.blue_fill_selected_bg);
            priority3.setTextAppearance(this, R.style.selected_priority_blue);
        } else {
            priority1Value = 1;
            priority1.setBackgroundResource(R.drawable.red_fill_selected_bg);
            priority1.setTextAppearance(this, R.style.selected_priority_red);
        }

        //Rotate Animation
        rotateAnimation = ObjectAnimator.ofFloat(micRecorder, View.ROTATION, 360);
        rotateAnimation.setRepeatCount(1);
        rotateAnimation.setRepeatMode(ValueAnimator.REVERSE);

        //Scale Animation
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.4f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.4f);
        ObjectAnimator scaleAnimation = ObjectAnimator.ofPropertyValuesHolder(micRecorder, pvhX, pvhY);
        scaleAnimation.setupStartValues();
        scaleAnimation.setRepeatCount(1);


        //set Of rotateAnimation, play together
        List<Animator> items = new ArrayList<>();
        items.add(scaleAnimation);
        items.add(rotateAnimation);
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(items);

        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        micRecorder.setOnClickListener(this);

    }

    void deleteTask() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.projectTaskDao().deleteTaskByid(taskId);
            }
        });
        finish();
    }

    private void initLayout() {
        imgNotification = findViewById(R.id.notification_img);
        projectName = findViewById(R.id.project_name);
        projectDesc = findViewById(R.id.project_description);

        dateTime = findViewById(R.id.date_time);
        taskTitle = findViewById(R.id.task_title);
        taskDesc = findViewById(R.id.task_description);
        micRecorder = findViewById(R.id.img_mic);
        priority1 = findViewById(R.id.priority_1);
        priority2 = findViewById(R.id.priority_2);
        priority3 = findViewById(R.id.priority_3);
        saveBtn = findViewById(R.id.save_btn);
        deleteBtn = findViewById(R.id.delete_btn);

        playBtn = findViewById(R.id.img_play);

        addTaskContainer = findViewById(R.id.add_task_container);
    }

    private void writeTask() {

        dateTimeText = dateTime.getText().toString();
        String taskTitleText = taskTitle.getText().toString();
        String taskDescText = taskDesc.getText().toString();

        if (taskTitleText.isEmpty()) {
            Snackbar.make(addTaskContainer, "Title is empty", Snackbar.LENGTH_SHORT).show();
            return;
        }

        final Task task;


        if (isUpdating) {
            task = new Task(taskId, projectRowId, timeInMilli, dateTimeText, taskTitleText, taskDescText,
                    priority1Value, filePath1);
        } else {
            task = new Task(projectRowId, timeInMilli, dateTimeText, taskTitleText, taskDescText,
                    priority1Value, filePath1);
        }

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.projectTaskDao().insertTaskByProjectId(task);
            }
        });

        if(timeInMilli != -1){ //if the user set the notification
            Long l = Long.valueOf(projectRowId);
            int i = l.intValue();
            createNotification(i, task);
        }

        finish();
    }

    private void createNotification(int notifyId, Task taskForNotify) {

        Intent notifyIntent = new Intent(this, AddTaskActivity.class);

        notifyIntent.putExtra(CURRENT_PROJECT, project);
        notifyIntent.putExtra(CURRENT_TASK, taskForNotify);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                notifyId, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.projectreminder_logo)
                .setContentTitle(taskForNotify.getTitle())
                .setContentText(taskForNotify.getDescription())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true) //notification disappear when clicked
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));


        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, AlertReceiver.class);
        notificationIntent.putExtra(NOTIFICATION_BUILT, builder.build());
        notificationIntent.putExtra(NOTIFICATION_ID, notifyId);

        PendingIntent broadcastPendingIntent = PendingIntent.getBroadcast(this,
                notifyId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int adjustTime = sharedPreferences.getInt(RB_VALUE, 0);

        long triggerTime = timeInMilli - adjustTime;
        if( triggerTime < 0 ){ //if triggerTIme is a negative value
            triggerTime = timeInMilli;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, taskForNotify.getNotification(), broadcastPendingIntent);
        }else{
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, broadcastPendingIntent);

        }

    }


    private void cancelAlarm(int notifyId) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, AlertReceiver.class);
        PendingIntent broadcastPendingIntent = PendingIntent.getBroadcast(this,
                notifyId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(broadcastPendingIntent);

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Close on error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
        //dayOfWeek = getSelectedDayOfWeek(year, month, dayOfMonth);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_time:
                break;
            case R.id.notification_img:
                //v.setBackgroundResource(R.drawable.ic_notifications_fill);
                if(dateTimeText.isEmpty()){
                    DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                            R.style.datePicker, this, year, month, day);
                    datePickerDialog.show();
                }else {
                    dateTimeText = "";
                    dateTime.setText(dateTimeText);
                    imgNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications_none));
                    timeInMilli = -1;
                    Long l = Long.valueOf(projectRowId);
                    int i = l.intValue();
                    cancelAlarm(i);
                }
                break;
            case R.id.save_btn:
                writeTask();
                break;
            case R.id.delete_btn:
                deleteTask();
                break;
            case R.id.priority_1:
                priority1Value = 1;
                setPriorityBg(priority1, priority2, priority3,
                        R.drawable.red_fill_selected_bg, R.style.selected_priority_red);
                break;
            case R.id.priority_2:
                priority1Value = 2;
                setPriorityBg(
                        priority2, priority1, priority3,
                        R.drawable.green_fill_selected_bg, R.style.selected_priority_green);
                break;
            case R.id.priority_3:
                priority1Value = 3;
                setPriorityBg(priority3, priority1, priority2,
                        R.drawable.blue_fill_selected_bg, R.style.selected_priority_blue);
                break;
            case R.id.img_mic:
                /*animatorSet.setDuration(1000);
                animatorSet.start();*/
                Log.d(TAG, "onClick: you clicked");

                break;
            case R.id.img_play:
                taskRecorder.playUserRecording(filePath1);
                break;
            default:
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setPriorityBg(TextView selectedPriority,
                               TextView unSelectedPriority1,
                               TextView unSelectedPriority2, int drawable, int style) {

        selectedPriority.setBackgroundResource(drawable);
        selectedPriority.setTextAppearance(this, style);

        unSelectedPriority1.setBackgroundResource(R.drawable.circular_border);
        unSelectedPriority1.setTextAppearance(this, R.style.unselected_priority);

        unSelectedPriority2.setBackgroundResource(R.drawable.circular_border);
        unSelectedPriority2.setTextAppearance(this, R.style.unselected_priority);

    }


    private String getSelectedDayOfWeek(int selectedYear, int selectedMonth, int selectedDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, selectedDay);
        return (new SimpleDateFormat("MMM E d").format(calendar.getTime()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String userDate = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
        Log.d(TAG, "onTimeSet: " + userDate);
        this.hour = hourOfDay;
        this.minute = minute;

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day,
                hour, minute, 0);
        timeInMilli = calendar.getTimeInMillis();
        dateTimeText = new SimpleDateFormat("E, MMM dd yyyy hh:mm a").format(calendar.getTime());

        if(!dateTimeText.isEmpty()){ //if dateTimeText is not empty
            imgNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications_fill));
            imgNotification.setAnimation(fadeIn);

        }

        dateTime.setText(dateTimeText);
    }

    public void showToast(View view, String message) {
        int x = view.getLeft();
        int y = view.getTop() + view.getHeight() - 40;
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.START, x, y);
        toast.show();
    }

    public void initTaskRecorder() {
        micRecorder.setOnLongClickListener(this);
        micRecorder.setOnTouchListener(this);
    }

    private void askUserForAppsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODES);
            Log.d(TAG, "askUserForAppsPermission: Permissions Were never granted ");
        } else {
            taskRecorder = new TaskRecorder(this);
            initTaskRecorder();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODES:
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            return;
                        } else {
                            taskRecorder = new TaskRecorder(this);
                            initTaskRecorder();
                        }
                    }
                }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Log.d(TAG, "onLongClick: Called");
        isSButtonLongPressed = true;
        filePath1 = taskRecorder.startRecordingUserTask(projectRowId);

        animatorSet.setDuration(1000);
        animatorSet.start();

        //micRecorder.setBackground(getResources().getDrawable(R.drawable.recording_drawable));
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.onTouchEvent(event);
        // We're only interested in when the button is just pressed Down.
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            showToast(v, "Hold To Record Release to Save");
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // We're only interested in anything if our speak button is currently pressed.
            if (isSButtonLongPressed) {
                // Do something when the button is released.
                taskRecorder.stopRecordingUserTask();
                //micRecorder.startAnimation(rotate_backward);
                //playBtn.startAnimation(fadeOut);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    animatorSet.reverse();
                }
                playBtn.setVisibility(View.VISIBLE);
                isSButtonLongPressed = false;
                //micRecorder.setBackground(getResources().getDrawable(R.drawable.rounded_image));
                Log.d(TAG, "onTouch: Button Has Been Released");
            }
        }
        return true;
    }
}

