package com.hashicode.simpletodo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by takahashi on 1/13/16.
 */
public final class Constants {

    public static final DateFormat SHORT_DATEFORMAT = DateFormat.getDateInstance(DateFormat.SHORT);
    public static final DateFormat TIME_DATEFORMAT = DateFormat.getTimeInstance(DateFormat.SHORT);

    public static final String CURRENT_VIEW_BUNDLE = "currentView";
    public static final String DATE_START_BUNDLE = "start";
    public static final String DATE_END_BUNDLE = "end";

    public static final String TASK_BUNDLE = "task";
    public static final String TASK_REMIND_BUNDLE = "task_remind";
    public static final String TASK_ID_BUNDLE = "task_id";
    public static final String TASK_NAME_BUNDLE = "task_task_description";

    public static final String INSERT="insert";
    public static final String UPDATE="update";
    public static final String DELETE="delete";
    public static final String DONE="done";

    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");

    public static final String START="start";
    public static final String END="end";

    public static final String TASK_PREFERENCES = "taskPreferences";
    public static final String TASKS_IDS_REMOVE = "tasksIdsRemove";
    public static final String TASK_NOTIFICATION_DESCRIPTIONS = "taskNotificationDescriptions";

}
