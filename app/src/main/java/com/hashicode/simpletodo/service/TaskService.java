package com.hashicode.simpletodo.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.*;

import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import com.hashicode.simpletodo.Constants;
import com.hashicode.simpletodo.model.Task;
import com.hashicode.simpletodo.model.TaskTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Set;

/**
 * Service that is responsable for crud operations (delegate to the {@link TaskContentProvider}) and (un)schedule tasks.
 *
 * Created by takahashi on 11/3/15.
 */
public class TaskService {

    private static TaskService serviceInstance = null;

    private Context context;
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");

    private TaskService(Context context){
        this.context = context;
    }

    public static TaskService getInstance(Context context){
        if(serviceInstance ==null){
            serviceInstance = new TaskService(context);
        }
        return serviceInstance;
    }

    /**
     * Insert a {@link Task}. Schedule if there is a remind .
     * @param task
     * @return the id of the inserted {@link Task}
     */
    public long insert(Task task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskTable.TASK_NAME_COLUMN, task.getName());
        if(task.getRemind()!=null){
            contentValues.put(TaskTable.REMIND_COLUMN,DATE_FORMAT.format(task.getRemind()));
        }
        Uri insert = context.getContentResolver().insert(UriUtils.buildTask(), contentValues);
        long id = UriUtils.getIdFromUri(insert);
        task.setId(id);
        this.scheduleTask(task);
        return id;
    }

    /**
     * Update a {@link Task}. Schedule if there is a remind .
     * @param task
     * @return 1 (number of rows updated)
     */
    public int update(Task task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskTable.TASK_NAME_COLUMN, task.getName());
        if(task.getRemind()!=null){
            contentValues.put(TaskTable.REMIND_COLUMN,DATE_FORMAT.format(task.getRemind()));
        }
        else{
            contentValues.putNull(TaskTable.REMIND_COLUMN);
        }
        int update = context.getContentResolver().update(UriUtils.buildTaskWithId(task.getId()), contentValues, null, null);
        this.scheduleTask(task);
        return update;
    }

    /**
     * Delete a {@link Task}. Unschedule if there is a remind.
     *
     * @param taskId
     * @return 1 (number of rows deleted)
     */
    public int delete(Long taskId){
        this.unscheduleTask(taskId.toString());
        return context.getContentResolver().delete(UriUtils.buildTaskWithId(taskId),null,null);
    }

    /**
     * Mark a  {@link Task} as done (for now, just delete the task).
     *
     * @param taskId
     * @return
     */
    public int done(Long taskId){
        return context.getContentResolver().delete(UriUtils.buildTaskWithId(taskId),null,null);
    }

    /**
     * Schedule an alarm if the task have an remind. Cancel otherwise.
     *
     * @param task
     */
    private void scheduleTask(Task task){
        AlarmManager alarmManager =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = buildPendingIntent(task);
        if(task.getRemind()!=null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, task.getRemind().getTime(), pendingIntent);
        }
        else{
            alarmManager.cancel(pendingIntent);
        }
    }

    /**
     * Build a {@link PendingIntent} that call {@link TaskNotificationIntentService} used by the alarm.
     * @param task
     * @return
     */
    private PendingIntent buildPendingIntent(Task task) {
        Intent intent =  new Intent(this.context, TaskNotificationIntentService.class).
                putExtra(Constants.TASK_REMIND_BUNDLE, task.getRemind()).
                putExtra(Constants.TASK_NAME_BUNDLE, task.getName()).
                putExtra(Constants.TASK_ID_BUNDLE, task.getId());
        return PendingIntent.getService(this.context, task.getId().toString().hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void unscheduleTask(String taskIdAsString){
        PendingIntent pendingIntent = PendingIntent.getService(this.context, taskIdAsString.hashCode(), new Intent(this.context, TaskNotificationIntentService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * Schedule all tasks thata gave remind on boot.
     */
    protected void scheduleTasksOnBoot(){
        Cursor cursor = context.getContentResolver().query(UriUtils.buildTaksWithDate(), null, null, null, null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            Task task = new Task();
            task.setId(cursor.getLong(TaskTable.ID_COLUMN_IDX));
            task.setName(cursor.getString(TaskTable.TASK_NAME_COLUMN_IDX));
            try {
                String dateAsString = cursor.getString(TaskTable.REMIND_COLUMN_IDX);
                task.setRemind(Constants.DATE_FORMAT.parse(dateAsString));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            scheduleTask(task);
        }
        cursor.close();
    }

    /**
     * Mark many {@link Task} as done.
     * @param ids
     */
    public void bulkDone(Set<String> ids) {
        ArrayList<ContentProviderOperation> operation = new ArrayList<>();
        for(String id : ids){
            unscheduleTask(id);
            operation.add(ContentProviderOperation.newDelete(UriUtils.buildTaskWithId(Long.parseLong(id))).build());
        }
        try {
            context.getContentResolver().applyBatch(TaskContentProvider.CONTENT_AUTHORITY, operation);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }
}
