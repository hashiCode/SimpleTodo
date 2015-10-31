package com.hashicode.simpletodo.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hashicode.simpletodo.Constants;
import com.hashicode.simpletodo.R;
import com.hashicode.simpletodo.model.Task;

import java.util.Calendar;
import java.util.Date;

/**
 * {@link DialogFragment} that show a {@link TimePickerDialogFragment} to select the remind value for {@link Task}.
 *
 * Created by takahashi on 31/01/16.
 */
public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    private Date remind;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.remind = (Date) getArguments().getSerializable(Constants.TASK_REMIND_BUNDLE);
        final Calendar c = Calendar.getInstance();
        if(this.remind !=null) {
            c.setTime(remind);
        }
        else{
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hourOfDay, minutes, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView dateRemindTextView = (TextView) getActivity().findViewById(R.id.timeremind_value);
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        dateRemindTextView.setText(Constants.TIME_DATEFORMAT.format(c.getTime()));
    }

}
