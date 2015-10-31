package com.hashicode.simpletodo.fragment;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hashicode.simpletodo.Constants;
import com.hashicode.simpletodo.R;
import com.hashicode.simpletodo.model.Task;
import com.hashicode.simpletodo.model.TaskTable;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link RecyclerView.Adapter} that use a {@link Cursor}.
 *
 * Created by takahashi on 11/9/15.
 */
public class TasksCursorAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private List<Task> tasks = new ArrayList<>();
    private View tasksContainer;
    private Cursor cursor;

    public TasksCursorAdapter(View tasksContainer) {
        this.tasksContainer = tasksContainer;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_task, parent, false);
        return new TaskViewHolder(view, this, tasksContainer);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.getTaskName().setText(task.getName());
        holder.setTask(task);
        holder.getDoneCheckBox().setChecked(false);
    }

    /**
     * Change the {@link Cursor} hold by this adapter.
     * @param otherCursor
     */
    public void swapCursor(Cursor otherCursor){
        if(otherCursor!=null) {
            if(this.cursor!=null) {
                this.cursor.close();
            }
            this.cursor=otherCursor;
            this.tasks.clear();
            for (int i = 0; i < otherCursor.getCount(); i++) {
                otherCursor.moveToPosition(i);
                Task task = new Task();
                task.setId(otherCursor.getLong(TaskTable.ID_COLUMN_IDX));
                task.setName(otherCursor.getString(TaskTable.TASK_NAME_COLUMN_IDX));
                try {
                    String dateAsString = otherCursor.getString(TaskTable.REMIND_COLUMN_IDX);
                    if (dateAsString != null) {
                        task.setRemind(Constants.DATE_FORMAT.parse(dateAsString));
                    } else {
                        task.setRemind(null);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tasks.add(task);
            }
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public List<Task> getTasks(){
        return tasks;
    }
}
