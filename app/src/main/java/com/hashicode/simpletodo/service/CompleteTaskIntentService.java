package com.hashicode.simpletodo.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.hashicode.simpletodo.Constants;

/**
 * An {@link IntentService} called when the user press "done" on a notification
 *
 * Created by takahashi on 2/17/16.
 */
public class CompleteTaskIntentService extends IntentService {

    private static final String SERVICE_NAME=CompleteTaskIntentService.class.getSimpleName();

    public CompleteTaskIntentService(){
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        Long taskId = (Long) extras.getSerializable(Constants.TASK_ID_BUNDLE);
        TaskService.getInstance(getApplicationContext()).done(taskId);
        Context context = this.getApplicationContext();

        NotificationClearHelper.clearNotification(context);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(TaskNotificationIntentService.NOTIFICATION_ID);
    }
}
