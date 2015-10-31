package com.hashicode.simpletodo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hashicode.simpletodo.Constants;
import com.hashicode.simpletodo.R;
import com.hashicode.simpletodo.fragment.TaskFragment;
import com.hashicode.simpletodo.model.Task;
import com.hashicode.simpletodo.service.AsyncTaskService;
import com.hashicode.simpletodo.service.AsyncTaskServiceCallback;

/**
 * Created by takahashi on 11/11/15.
 */
public class TaskActivity extends AppCompatActivity {

    private Task task;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeTodo(savedInstanceState);
        setContentView(R.layout.activity_task);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
    }

    /**
     * Extract the {@link Task} from the savedInstanceState.
     * If does not exists, initialize one (create).
     * @param savedInstanceState
     */
    private void initializeTodo(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if(savedInstanceState==null) {
            if (extras != null) {
                Task task = extras.getParcelable(Constants.TASK_BUNDLE);
                this.task = task;
            } else {
                this.task = new Task();
            }
        }
        else {
            this.task = savedInstanceState.getParcelable(Constants.TASK_BUNDLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        TaskFragment taskFragment = (TaskFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_task);
        taskFragment.updateTask(task);
        outState.putParcelable(Constants.TASK_BUNDLE, task);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(this.task.getId()!=null){
            getMenuInflater().inflate(R.menu.menu_task_create_edit, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete :
                AsyncTaskService asyncTaskService = new AsyncTaskService(this, new AsyncTaskServiceCallback() {
                    @Override
                    public void onPostExecute(Void aVoid) {
                        finish();
                    }
                });
                asyncTaskService.execute(AsyncTaskService.Operation.buildDeleteOperation(task.getId()));
                break;
        }
        finish();
        return true;
    }

    /**
     *
     * @return the task of this activity
     */
    public Task getTask(){
        return this.task;
    }
}
