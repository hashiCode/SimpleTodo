package com.hashicode.simpletodo.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hashicode.simpletodo.model.TaskTable;

/**
 * Created by takahashi on 11/4/15.
 */
public class TaskDatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "simpletodo.db";
    private static final int DB_VERSION = 1;

    private static TaskDatabaseHelper TODO_DATABASE_HELPER_INSTANCE = null;

    private TaskDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    public static TaskDatabaseHelper getInstance(Context context) {
        if (TODO_DATABASE_HELPER_INSTANCE == null) {
            TODO_DATABASE_HELPER_INSTANCE = new TaskDatabaseHelper(context);
        }
        return TODO_DATABASE_HELPER_INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TaskTable.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}