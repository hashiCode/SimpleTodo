package com.hashicode.simpletodo.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by takahashi on 3/1/16.
 */
public class TaskBootStartupIntentService extends IntentService {

    public TaskBootStartupIntentService(){
        super(TaskBootStartupIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationClearHelper.clearNotification(this.getApplicationContext());
        TaskService.getInstance(this.getApplicationContext()).scheduleTasksOnBoot();
    }
}
