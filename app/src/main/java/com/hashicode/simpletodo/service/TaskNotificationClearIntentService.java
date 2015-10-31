package com.hashicode.simpletodo.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * Created by takahashi on 2/25/16.
 */
public class TaskNotificationClearIntentService extends IntentService {

    public TaskNotificationClearIntentService() {
        super(TaskNotificationClearIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context context =this.getApplicationContext();
        NotificationClearHelper.clearNotification(context);
    }
}
