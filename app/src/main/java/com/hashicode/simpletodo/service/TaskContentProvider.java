package com.hashicode.simpletodo.service;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import com.hashicode.simpletodo.Constants;
import com.hashicode.simpletodo.model.TaskTable;

import java.util.Date;

/**
 * Created by takahashi on 2/18/16.
 */
public class TaskContentProvider extends ContentProvider {

    //paths
    static final String TASKS_PATH = "tasks";
    static final String TAKS_WITH_INTERVAL_PATH = "todos_interval";
    static final String TASK_PATH = "todo";
    static final String TASKS_WITH_DATE_PATH = "todos_date";
    static final String TASKS_LATE_PATH = "todos_late";
    //uris
    public static final String CONTENT_AUTHORITY = "com.hashicode.simpletodo";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final Uri TASKS_WITH_INTERVAL_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TAKS_WITH_INTERVAL_PATH).build();
    static final Uri TASKS_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TASKS_PATH).build();
    static final Uri TASK_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TASK_PATH).build();
    static final Uri TASKS_WITH_DATE_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TASKS_WITH_DATE_PATH).build();
    static final Uri TASKS_LATE_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TASKS_LATE_PATH).build();
    //codes
    static final int TASK = 0;
    static final int TASKS_WITH_INTERVAL = 1;
    static final int TASKS = 2;
    static final int TASK_WITH_ID = 3;
    static final int TASKS_WITH_DATE = 4;
    static final int TASKS_LATE = 5;


    private TaskDatabaseHelper taskDatabaseHelper;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(CONTENT_AUTHORITY, TASKS_PATH, TASKS);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, TASK_PATH, TASK);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, TASK_PATH +"/#", TASK_WITH_ID);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, TAKS_WITH_INTERVAL_PATH, TASKS_WITH_INTERVAL);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, TASKS_WITH_DATE_PATH, TASKS_WITH_DATE);
        URI_MATCHER.addURI(CONTENT_AUTHORITY, TASKS_LATE_PATH, TASKS_LATE);
    }


    @Override
    public boolean onCreate() {
        taskDatabaseHelper = TaskDatabaseHelper.getInstance(this.getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int code = URI_MATCHER.match(uri);
        Cursor cursor;
        switch (code){
            case TASKS_WITH_INTERVAL:
                Date start = UriUtils.getStartFromUri(uri);
                Date end = UriUtils.getEndFromUri(uri);
                cursor =this.taskDatabaseHelper.getReadableDatabase().query(TaskTable.TABLE_NAME, projection, TaskTable.CLAUSE_BY_INTERVAL,new String[]{Constants.DATE_FORMAT.format(start), Constants.DATE_FORMAT.format(end)},null,null, TaskTable.REMIND_COLUMN);
                break;
            case TASKS:
                cursor =this.taskDatabaseHelper.getReadableDatabase().query(TaskTable.TABLE_NAME, projection,null,null,null,null, TaskTable.REMIND_COLUMN);
                break;
            case TASKS_WITH_DATE:
                cursor =this.taskDatabaseHelper.getReadableDatabase().query(TaskTable.TABLE_NAME, projection, TaskTable.CLAUSE_HAS_DATE,null,null,null, TaskTable.REMIND_COLUMN);
                break;
            case TASKS_LATE:
                cursor =this.taskDatabaseHelper.getReadableDatabase().query(TaskTable.TABLE_NAME, projection, TaskTable.CLAUSE_LATE,null,null,null, TaskTable.REMIND_COLUMN);
                break;
            default :
                throw new IllegalArgumentException(uri.toString()+" - "+code);
        }
        cursor.setNotificationUri(this.getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = URI_MATCHER.match(uri);
        switch (match){
            case TASK:
                long id = taskDatabaseHelper.getWritableDatabase().insert(TaskTable.TABLE_NAME, null, values);
                notifyChanges();
                return UriUtils.buildTaskWithId(id);

            default :
                throw new IllegalArgumentException(uri.toString() + " "+match);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = URI_MATCHER.match(uri);
        switch (match){
             case TASK_WITH_ID:
                 Long id = UriUtils.getIdFromUri(uri);
                 int delete = this.taskDatabaseHelper.getWritableDatabase().delete(TaskTable.TABLE_NAME, TaskTable.ID_COLUMN + "=?", new String[]{id.toString()});
                 if(delete>0){
                     notifyChanges();
                 }
                 return delete;
            default :
        }        throw new IllegalArgumentException(uri.toString() + " "+match);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = URI_MATCHER.match(uri);
        switch (match){
            case TASK_WITH_ID:
                Long id = UriUtils.getIdFromUri(uri);
                int update = this.taskDatabaseHelper.getWritableDatabase().update(TaskTable.TABLE_NAME, values, TaskTable.ID_COLUMN + "=?", new String[]{id.toString()});
                if(update>0){
                    notifyChanges();
                }
                return update;
            default :
        }       throw new IllegalArgumentException(uri.toString() + " "+match);
    }

    private void notifyChanges() {
        getContext().getContentResolver().notifyChange(TASKS_CONTENT_URI, null);
        getContext().getContentResolver().notifyChange(TASKS_WITH_INTERVAL_CONTENT_URI, null);
        getContext().getContentResolver().notifyChange(TASKS_LATE_CONTENT_URI, null);
    }
}
