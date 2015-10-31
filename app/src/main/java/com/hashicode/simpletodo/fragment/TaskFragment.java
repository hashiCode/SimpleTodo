package com.hashicode.simpletodo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.hashicode.simpletodo.Constants;
import com.hashicode.simpletodo.R;
import com.hashicode.simpletodo.activity.TaskActivity;
import com.hashicode.simpletodo.model.Task;
import com.hashicode.simpletodo.service.AsyncTaskService;
import com.hashicode.simpletodo.service.AsyncTaskServiceCallback;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by takahashi on 11/11/15.
 */
public class TaskFragment extends Fragment{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_task, container, false);
        initializeView(inflate, ((TaskActivity)this.getActivity()).getTask());
        FloatingActionButton fab = (FloatingActionButton) inflate.findViewById(R.id.fab_done_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = ((TaskActivity) getActivity()).getTask();
                updateTask(task);
                AsyncTaskService asyncTaskService = new AsyncTaskService(getContext(), new AsyncTaskServiceCallback() {
                    @Override
                    public void onPostExecute(Void aVoid) {
                        getActivity().finish();
                    }
                });
                if(task.getId()==null){
                    asyncTaskService.execute(AsyncTaskService.Operation.buildInsertOperation(task));
                }
                else{
                    asyncTaskService.execute(AsyncTaskService.Operation.buildUpdateOperation(task));
                }
            }
        });
        return inflate;
    }

    /**
     * Initialize the view with the {@link Task} from the {@link TaskActivity}.
     *
     * @param inflate
     * @param task
     */
    private void initializeView(View inflate, final Task task) {
        EditText todoNameEditText = (EditText) inflate.findViewById(R.id.task_name);
        final TextView dateRemindValueTextView = (TextView) inflate.findViewById(R.id.dateremind_value);
        final ImageView cancelImageView = (ImageView) inflate.findViewById(R.id.erase_dateremind_image);
        dateRemindValueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dateDialog = new DatePickerDialogFragment();
                Bundle extra = new Bundle();
                extra.putSerializable(Constants.TASK_REMIND_BUNDLE, task.getRemind());
                dateDialog.setArguments(extra);
                dateDialog.show(getActivity().getSupportFragmentManager(),"");
            }
        });
        final TextView timeRemindValueTextView = (TextView) inflate.findViewById(R.id.timeremind_value);
        final TextView timeRemindLabelTextView = (TextView) inflate.findViewById(R.id.timeremind_label);
        timeRemindValueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dateDialog = new TimePickerDialogFragment();
                Bundle extra = new Bundle();
                extra.putSerializable(Constants.TASK_REMIND_BUNDLE, task.getRemind());
                dateDialog.setArguments(extra);
                dateDialog.show(getActivity().getSupportFragmentManager(),"");
            }
        });
        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateRemindValueTextView.setText(R.string.task_remind_none);
                timeRemindValueTextView.setText(R.string.task_remind_none);
                timeRemindValueTextView.setClickable(false);
                timeRemindValueTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.Disabled));
                timeRemindLabelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.Disabled));
                cancelImageView.setVisibility(View.INVISIBLE);
            }
        });

        if(task.getRemind()!=null){
            dateRemindValueTextView.setText(Constants.SHORT_DATEFORMAT.format(task.getRemind()));
            timeRemindValueTextView.setText(Constants.TIME_DATEFORMAT.format(task.getRemind()));
            timeRemindValueTextView.setClickable(true);
        }
        else{
            cancelImageView.setVisibility(View.INVISIBLE);
            timeRemindValueTextView.setClickable(false);
            timeRemindValueTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.Disabled));
            timeRemindLabelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.Disabled));
        }
        todoNameEditText.setText(task.getName());
    }

    /**
     * Update {@link Task} from the view of this fragment.
     * @param task
     */
    public void updateTask(Task task){
        View view = this.getView();
        EditText todoNameEditText = (EditText) view.findViewById(R.id.task_name);
        TextView dateRemindValueTextView = (TextView) view.findViewById(R.id.dateremind_value);
        TextView timeRemindValueTextView = (TextView) view.findViewById(R.id.timeremind_value);
        task.setName(todoNameEditText.getText().toString());
        if(!dateRemindValueTextView.getText().toString().equals(getContext().getResources().getString(R.string.task_remind_none))){
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(Constants.SHORT_DATEFORMAT.parse(dateRemindValueTextView.getText().toString()));
                Calendar hour = Calendar.getInstance();
                hour.setTime(Constants.TIME_DATEFORMAT.parse(timeRemindValueTextView.getText().toString()));
                calendar.set(Calendar.HOUR_OF_DAY, hour.get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, hour.get(Calendar.MINUTE));
                task.setRemind(calendar.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else{
            task.setRemind(null);
        }
    }

}
