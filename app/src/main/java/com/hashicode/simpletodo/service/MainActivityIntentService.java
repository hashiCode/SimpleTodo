package com.hashicode.simpletodo.service;

import android.app.IntentService;
import android.content.Intent;
import com.hashicode.simpletodo.activity.MainActivity;

/**
 * An {@link IntentService} that is called when the user press the notification.
 * Start the {@link MainActivity}.
 *
 * Created by takahashi on 2/29/16.
 */
public class MainActivityIntentService extends IntentService {

    public MainActivityIntentService(){
        super(MainActivityIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationClearHelper.clearNotification(this.getApplicationContext());
        Intent intentActivity = new Intent(this.getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentActivity);
    }
}
