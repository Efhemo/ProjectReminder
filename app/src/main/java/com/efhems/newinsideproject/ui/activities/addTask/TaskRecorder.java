package com.efhems.newinsideproject.ui.activities.addTask;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.efhems.newinsideproject.R;

import java.io.IOException;

public class TaskRecorder {
    private MediaRecorder recorder;
    private static final String TAG = "NoteRecorder";
    private String filePath;
    private Context context;

    public TaskRecorder(Context context) {
        this.context = context;
    }

    public String startRecordingUserTask(long projectId) {
        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + projectId + " task.3gp";
            recorder.setOutputFile(filePath);
            recorder.prepare();
            recorder.start();
            showToast(context, context.getString(R.string.record_started));
        } catch (IOException e) {
            Log.d(TAG, "recordUserTask Exception : " + e.getMessage());
        }

        return filePath;
    }

    public void stopRecordingUserTask() {
        recorder.stop();
        // MediaRecorder should not be release here
        // That's what caused the App to crash
        recorder = null;
        showToast(context, context.getString(R.string.record_stop));
    }

    public void playUserRecording(String filePath) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            showToast(context, context.getString(R.string.play));
        } catch (IOException e) {

            Log.d(TAG, "playUserRecording: Exception " + e.getMessage());
        }
    }

    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
