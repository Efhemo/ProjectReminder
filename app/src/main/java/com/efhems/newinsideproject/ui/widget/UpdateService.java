package com.efhems.newinsideproject.ui.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.efhems.newinsideproject.data.local.entities.Project;

import java.util.ArrayList;

public class UpdateService extends IntentService {

    public static String FROM_ACTIVITY_PROJECT_LIST ="FROM_ACTIVITY_PROJECT_LIST";

    public UpdateService() {
        super("UpdateService");
    }

    public static void startService(Context context, ArrayList<Project> fromActivityProjectsList) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.putExtra(FROM_ACTIVITY_PROJECT_LIST, fromActivityProjectsList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<Project> fromActivityProjectsList = intent.getExtras().getParcelableArrayList(FROM_ACTIVITY_PROJECT_LIST);
            handleActionUpdateProjectWidgets(fromActivityProjectsList);

        }
    }

    /*reciever*/
    private void handleActionUpdateProjectWidgets(ArrayList<Project> fromActivityProjectsList) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra(FROM_ACTIVITY_PROJECT_LIST,fromActivityProjectsList);
        sendBroadcast(intent);
    }

}