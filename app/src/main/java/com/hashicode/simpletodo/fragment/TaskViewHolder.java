package com.hashicode.simpletodo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.hashicode.simpletodo.Constants;
import com.hashicode.simpletodo.R;
import com.hashicode.simpletodo.activity.TaskActivity;
import com.hashicode.simpletodo.model.Task;

/**
 * Created by takahashi on 11/9/15.
 */
public class TaskViewHolder extends RecyclerView.ViewHolder {

    private TextView taskName;
    private Task task;
    private CheckBox doneCheckBox;
    private final TasksCursorAdapter tasksCursorAdapter;
    private final View tasksContainer;

    public TaskViewHolder(View itemView, final TasksCursorAdapter tasksCursorAdapter, View tasksContainer) {
        super(itemView);
        this.tasksContainer = tasksContainer;
        taskName = (TextView) itemView.findViewById(R.id.task_name);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.TASK_BUNDLE, task);
                Intent intent = new Intent(v.getContext(), TaskActivity.class);
                intent.putExtra(Constants.TASK_BUNDLE, task);
                v.getContext().startActivity(intent);
            }
        });
        this.tasksCursorAdapter = tasksCursorAdapter;
        doneCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox_done);
        DoneCheckBoxClickListener checkListener = new DoneCheckBoxClickListener(this);
        doneCheckBox.setOnCheckedChangeListener(checkListener);

    }

    public TextView getTaskName() {
        return taskName;
    }

    public void setTask(Task task){
        this.task = task;
    }

    public TasksCursorAdapter getTasksCursorAdapter() {
        return tasksCursorAdapter;
    }

    protected View getTasksContainer(){
        return this.tasksContainer;
    }

    protected Task getTask(){
        return this.task;
    }

    public CheckBox getDoneCheckBox() {
        return doneCheckBox;
    }
}
