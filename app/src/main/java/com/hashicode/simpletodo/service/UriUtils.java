package com.hashicode.simpletodo.service;

import android.content.ContentUris;
import android.net.Uri;
import com.hashicode.simpletodo.Constants;
import com.hashicode.simpletodo.R;

import java.util.Date;

/**
 * Created by takahashi on 2/22/16.
 */
public final class UriUtils {

    public static Uri buildTasksWithInterval(Date start, Date end, int viewType){
        if(start==null){
            if(viewType!= R.id.tasks_late) {
                return TaskContentProvider.TASKS_CONTENT_URI;
            }
            else{
                return TaskContentProvider.TASKS_LATE_CONTENT_URI;
            }
        }
        return TaskContentProvider.TASKS_WITH_INTERVAL_CONTENT_URI.buildUpon().appendQueryParameter(Constants.START, new Long(start.getTime()).toString()).appendQueryParameter(Constants.END, new Long(end.getTime()).toString()).build();
    }

    public static Uri buildTaskWithId(Long id){
        return ContentUris.withAppendedId(TaskContentProvider.TASK_CONTENT_URI, id).buildUpon().build();
    }

    public static Uri buildTask(){
        return TaskContentProvider.TASK_CONTENT_URI;
    }

    public static Long getIdFromUri(Uri uri){
        return new Long(uri.getPathSegments().get(1));
    }

    public static Date getStartFromUri(Uri uri){
        String s = uri.getQueryParameter(Constants.START);
        return new Date(new Long(s));
    }

    public static Date getEndFromUri(Uri uri){
        String s = uri.getQueryParameter(Constants.END);
        return new Date(new Long(s));
    }

    public static Uri buildTaksWithDate(){
        return TaskContentProvider.TASKS_WITH_DATE_CONTENT_URI;
    }

}
