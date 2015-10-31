package com.hashicode.simpletodo.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import com.hashicode.simpletodo.model.Task;
import com.hashicode.simpletodo.Constants;
import com.hashicode.simpletodo.R;

import java.util.Calendar;
import java.util.Date;

/**
 * {@link DialogFragment} that show a {@link DatePickerDialog} to select the remind for {@link Task}.
 *
 * Created by takahashi on 1/29/16.
 */
public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

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
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog =new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        TextView dateRemindTextView = (TextView) getActivity().findViewById(R.id.dateremind_value);
        TextView timeRemindTextView = (TextView) getActivity().findViewById(R.id.timeremind_value);
        TextView timeRemindLabelTextView = (TextView) getActivity().findViewById(R.id.timeremind_label);
        ImageView cancelImageView = (ImageView) getActivity().findViewById(R.id.erase_dateremind_image);
        cancelImageView.setVisibility(View.VISIBLE);
        timeRemindTextView.setClickable(true);
        timeRemindLabelTextView.setTextColor(ContextCompat.getColor(getContext(), android.support.v7.appcompat.R.color.secondary_text_default_material_dark));
        timeRemindTextView.setTextColor(ContextCompat.getColor(getContext(), android.support.v7.appcompat.R.color.primary_text_default_material_dark));
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateRemindTextView.setText(Constants.SHORT_DATEFORMAT.format(c.getTime()));
        timeRemindTextView.setText(Constants.TIME_DATEFORMAT.format(c.getTime()));
    }
}
