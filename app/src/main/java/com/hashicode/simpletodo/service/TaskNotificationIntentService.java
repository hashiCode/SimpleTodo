package com.hashicode.simpletodo.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.hashicode.simpletodo.Constants;
import com.hashicode.simpletodo.R;

import java.util.LinkedHashSet;
import java.util.Set;


/**
 * An {@link IntentService} reponsable for building {@link Notification}
 *
 * Created by takahashi on 09/02/16.
 */
public class TaskNotificationIntentService extends IntentService {

    protected final static int NOTIFICATION_ID = 1;
    private final static int MAX_DESCRIPTION_SIZE = 3;

    private final int PENDING_INTENT_COMPLETE_TASK =0;
    private final int PENDING_INTENT_DELETE=1;
    private final int PENDING_INTENT_MAIN_ACTIVITY=2;

    public TaskNotificationIntentService() {
        super(TaskNotificationIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = this.getApplicationContext();
        Bundle extras = intent.getExtras();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.TASK_PREFERENCES, Context.MODE_PRIVATE);
        Long taskId = (Long) extras.getSerializable(Constants.TASK_ID_BUNDLE);
        String description = (String) extras.getSerializable(Constants.TASK_NAME_BUNDLE);
        Set<String> descriptions = sharedPreferences.getStringSet(Constants.TASK_NOTIFICATION_DESCRIPTIONS, new LinkedHashSet<String>());
        descriptions.add(description);


        NotificationCompat.Builder builder = this.makeBuilder(descriptions, context, taskId);


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    /**
     * Make {@link NotificationCompat.Builder}. If there aren't any notification yet, build a simple one (with an action to done the task). Otherwise, return a builder
     * with all task description that are in a previous notification
     *
     * @param descriptions
     * @param context
     * @param taskId
     * @return
     */
    private NotificationCompat.Builder makeBuilder(Set<String> descriptions, Context context, Long taskId) {
        int count = descriptions.size();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).
                setSmallIcon(R.drawable.ic_access_alarm_white_24dp).
                setAutoCancel(true).setVisibility(NotificationCompat.VISIBILITY_PRIVATE).
                setDeleteIntent(buildDeletePendingIntent(context)).
                setContentIntent(buildMainActivityPendingIntent(context));

        if (count == 1) {
            String description = descriptions.iterator().next();
            builder.setContentText(description).
                    setContentTitle(context.getResources().getString(R.string.task_notification_single_title)).
                    addAction(R.drawable.ic_done_white_24dp, context.getResources().getString(R.string.task_done), buildCompleteTaskPendingIntent(taskId, context));
        } else {
            String title = String.format(context.getResources().getString(R.string.task_notification_multiple_title), descriptions.size());
            NotificationCompat.InboxStyle inboxStyle =
                    new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle(title);
            String[] descriptionsAsArray = descriptions.toArray(new String[descriptions.size()]);
            int rest = descriptionsAsArray.length - MAX_DESCRIPTION_SIZE;
            for (int i = 0; i < descriptionsAsArray.length && i < MAX_DESCRIPTION_SIZE; i++) {
                inboxStyle.addLine(descriptionsAsArray[i]);
            }
            if (rest > 0) {
                String summary = String.format(context.getResources().getString(R.string.task_notification_multiple_sumary), rest);
                inboxStyle.setSummaryText(summary);
            }
            builder.setStyle(inboxStyle);

        }
        return builder;
    }

    /**
     * Build a {@link PendingIntent} called when the "done" action happens.
     * @param taskId
     * @param context
     * @return
     */
    private PendingIntent buildCompleteTaskPendingIntent(Long taskId, Context context) {
        Intent intent = new Intent(context, CompleteTaskIntentService.class).
                putExtra(Constants.TASK_ID_BUNDLE, taskId);
        return PendingIntent.getService(context, PENDING_INTENT_COMPLETE_TASK, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Build a {@link PendingIntent} called when a notification is dismissed.
     * @param context
     * @return
     */
    private PendingIntent buildDeletePendingIntent(Context context) {
        Intent intent = new Intent(context, TaskNotificationClearIntentService.class);
        return PendingIntent.getService(context, PENDING_INTENT_DELETE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Build a {@link PendingIntent} called a notification is selected.
     * @param context
     * @return
     */
    private PendingIntent buildMainActivityPendingIntent(Context context) {
        Intent intent = new Intent(context, MainActivityIntentService.class);
        return PendingIntent.getService(context, PENDING_INTENT_MAIN_ACTIVITY, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }
}
