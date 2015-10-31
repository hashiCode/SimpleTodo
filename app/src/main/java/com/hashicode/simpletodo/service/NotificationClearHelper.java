package com.hashicode.simpletodo.service;

import android.content.Context;
import android.content.SharedPreferences;
import com.hashicode.simpletodo.Constants;
import com.hashicode.simpletodo.model.Task;

import java.util.LinkedHashSet;

/**
 * Created by takahashi on 2/29/16.
 */
public class NotificationClearHelper {

    /**
     * Clear the {@link Task} description when a notification is cleared.
     * @param context
     */
    public static void clearNotification(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.TASK_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putStringSet(Constants.TASK_NOTIFICATION_DESCRIPTIONS, new LinkedHashSet<String>()).commit();
    }
}
