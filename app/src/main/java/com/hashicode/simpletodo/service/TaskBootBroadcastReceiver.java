package com.hashicode.simpletodo.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hashicode.simpletodo.model.Task;

/**
 * A {@link BroadcastReceiver} called when the system boot.
 * Schedule all {@link Task}s tha have reminders.
 *
 * Created by takahashi on 3/1/16.
 */
public class TaskBootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, TaskBootStartupIntentService.class));
    }
}
