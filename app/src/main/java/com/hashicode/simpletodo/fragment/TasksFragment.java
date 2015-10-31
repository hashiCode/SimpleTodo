package com.hashicode.simpletodo.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hashicode.simpletodo.R;
import com.hashicode.simpletodo.activity.TaskActivity;
import com.hashicode.simpletodo.service.UriUtils;

import java.util.Date;

/**
 * Created by takahashi on 01/11/15.
 */
public class TasksFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String START = "intervalStart";
    public static final String END = "intervalEnd";
    public static final String VIEW_TYPE = "viewType";

    private RecyclerView tasksRecyclerView;
    private TasksCursorAdapter tasksCursorAdapter;
    private FloatingActionButton addTaskButton;
    private TextView emptyTasks;
    private int viewType;

    private Date start;
    private Date end;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        start = (Date) arguments.getSerializable(START);
        end = (Date) arguments.getSerializable(END);
        viewType = arguments.getInt(VIEW_TYPE);
        getLoaderManager().initLoader(0, null, this);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_tasks, null);
        View tasksView = inflate.findViewById(R.id.tasks_container);
        emptyTasks = (TextView) inflate.findViewById(R.id.empty_tasks);
        tasksRecyclerView = (RecyclerView) inflate.findViewById(R.id.tasks_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tasksRecyclerView.setLayoutManager(linearLayoutManager);
        tasksRecyclerView.setHasFixedSize(true);
        tasksCursorAdapter = new TasksCursorAdapter(tasksView);
        tasksRecyclerView.setAdapter(tasksCursorAdapter);
        addTaskButton = (FloatingActionButton) inflate.findViewById(R.id.fab_add_taks);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TaskActivity.class);
                startActivity(intent);
            }
        });
        switch (viewType) {
            case R.id.tasks_all:
                emptyTasks.setText(R.string.no_tasks_all);
                break;
            case R.id.tasks_today:
                emptyTasks.setText(R.string.no_tasks_today);
                break;
            case R.id.tasks_week:
                emptyTasks.setText(R.string.no_tasks_week);
                break;
            case R.id.tasks_month:
                emptyTasks.setText(R.string.no_tasks_month);
                break;
            case R.id.tasks_late:
                emptyTasks.setText(R.string.no_tasks_late);
                break;
        }

        return inflate;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = UriUtils.buildTasksWithInterval(this.start, this.end, this.viewType);
            return new CursorLoader(this.getContext(), uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        tasksCursorAdapter.swapCursor(data);
        if (data == null || data.getCount() == 0) {
            tasksRecyclerView.setVisibility(View.GONE);
            emptyTasks.setVisibility(View.VISIBLE);
        } else {
            tasksRecyclerView.setVisibility(View.VISIBLE);
            emptyTasks.setVisibility(View.GONE);
            tasksCursorAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        tasksCursorAdapter.swapCursor(null);
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }
}
