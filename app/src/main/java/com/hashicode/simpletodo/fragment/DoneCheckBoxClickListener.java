package com.hashicode.simpletodo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.hashicode.simpletodo.Constants;
import com.hashicode.simpletodo.R;
import com.hashicode.simpletodo.model.Task;
import com.hashicode.simpletodo.service.AsyncTaskService;
import com.hashicode.simpletodo.service.AsyncTaskServiceCallback;

import java.util.HashSet;
import java.util.Set;

/**
 * Listener to done a {@link Task}.
 *
 * Created by takahashi on 3/2/16.
 */
public class DoneCheckBoxClickListener implements OnCheckedChangeListener {

    private TaskViewHolder taskViewHolder;

    public DoneCheckBoxClickListener(TaskViewHolder taskViewHolder) {
        this.taskViewHolder = taskViewHolder;
    }

    @Override
    public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            final TasksCursorAdapter tasksCursorAdapter = taskViewHolder.getTasksCursorAdapter();
            final int position = taskViewHolder.getAdapterPosition();
            final Context context = buttonView.getContext();
            final SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.TASK_PREFERENCES, Context.MODE_PRIVATE);
            Set<String> idsSet = sharedPreferences.getStringSet(Constants.TASKS_IDS_REMOVE, new HashSet<String>());
            idsSet.add(taskViewHolder.getTask().getId().toString());
            sharedPreferences.edit().putStringSet(Constants.TASKS_IDS_REMOVE, idsSet).commit();
            tasksCursorAdapter.getTasks().remove(position);
            tasksCursorAdapter.notifyItemRemoved(position);

            Snackbar snackBarDone = Snackbar.make(taskViewHolder.getTasksContainer(),context.getResources().getString(R.string.task_done) , Snackbar.LENGTH_SHORT);
            snackBarDone.setAction(context.getResources().getString(R.string.task_dismiss), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tasksCursorAdapter.getTasks().add(position, taskViewHolder.getTask());
                    tasksCursorAdapter.notifyItemInserted(position);
                    Set<String> idsSet = sharedPreferences.getStringSet(Constants.TASKS_IDS_REMOVE, new HashSet<String>());
                    idsSet.remove(taskViewHolder.getTask().getId().toString());
                    sharedPreferences.edit().putStringSet(Constants.TASKS_IDS_REMOVE, idsSet).commit();
                }
            });
            snackBarDone.setCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    switch (event){
                        case DISMISS_EVENT_CONSECUTIVE :
                        case DISMISS_EVENT_TIMEOUT :
                        case DISMISS_EVENT_SWIPE :
                        case DISMISS_EVENT_MANUAL :
                            Set<String> idsRemove =sharedPreferences.getStringSet(Constants.TASKS_IDS_REMOVE, new HashSet<String>());
                            AsyncTaskService task = new AsyncTaskService(context, new AsyncTaskServiceCallback() {
                                @Override
                                public void onPostExecute(Void aVoid) {

                                }
                            });
                            task.execute(AsyncTaskService.Operation.buildBulkDoneOperation(idsRemove));
                            sharedPreferences.edit().putStringSet(Constants.TASKS_IDS_REMOVE, new HashSet<String>()).commit();
                            break;
                    }
                }
            });
            snackBarDone.show();


        }

    }
}
