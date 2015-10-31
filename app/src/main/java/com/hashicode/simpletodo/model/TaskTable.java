package com.hashicode.simpletodo.model;

import android.provider.BaseColumns;

/**
 * Created by takahashi on 11/4/15.
 */
public interface TaskTable {

    String TABLE_NAME="todo";

    String ID_COLUMN = BaseColumns._ID;
    String TASK_NAME_COLUMN = "name";
    String REMIND_COLUMN = "remind";

    int ID_COLUMN_IDX = 0;
    int TASK_NAME_COLUMN_IDX = 1;
    int REMIND_COLUMN_IDX = 2;

    String CLAUSE_BY_INTERVAL = TABLE_NAME+"."+ REMIND_COLUMN + " between ? and ?";
    String CLAUSE_HAS_DATE = TABLE_NAME+"."+ REMIND_COLUMN + " is not null";
    String CLAUSE_LATE = TABLE_NAME+"."+ REMIND_COLUMN + " <time('now', 'localtime')";

    String CREATE_TABLE = "create table "+TABLE_NAME+" ("+
            ID_COLUMN + " integer primary key, "+
            TASK_NAME_COLUMN + " text null,"+
            REMIND_COLUMN + " text)";
}
