package com.hashicode.simpletodo.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import com.hashicode.simpletodo.Constants;
import com.hashicode.simpletodo.R;
import com.hashicode.simpletodo.fragment.TasksFragment;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Launcher {@link AppCompatActivity}
 */
public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;

    private int currentView =-1;
    private Date start;
    private Date end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setupDrawer();
        if(savedInstanceState==null) {
            currentView = getDefaultView();
            setStartEnd();

        }else {
            currentView = savedInstanceState.getInt(Constants.CURRENT_VIEW_BUNDLE);
            start = (Date) savedInstanceState.getSerializable(Constants.DATE_START_BUNDLE);
            end = (Date) savedInstanceState.getSerializable(Constants.DATE_END_BUNDLE);
        }
        String toolbarTitle;
        switch (currentView){
            case R.id.tasks_all :
                toolbarTitle = getApplicationContext().getResources().getString(R.string.tasks_all);
                navigationView.setCheckedItem(R.id.tasks_all);
                break;
            case R.id.tasks_month :
                toolbarTitle = getApplicationContext().getResources().getString(R.string.tasks_month);
                navigationView.setCheckedItem(R.id.tasks_month);
                break;
            case R.id.tasks_today :
                toolbarTitle = getApplicationContext().getResources().getString(R.string.tasks_today);
                navigationView.setCheckedItem(R.id.tasks_today);
                break;
            case R.id.tasks_week :
                toolbarTitle = getApplicationContext().getResources().getString(R.string.tasks_week);
                navigationView.setCheckedItem(R.id.tasks_week);
                break;
            case R.id.tasks_late:
                toolbarTitle = getApplicationContext().getResources().getString(R.string.tasks_late);
                navigationView.setCheckedItem(R.id.tasks_late);
                break;
            default:
                throw new IllegalStateException();
        }
        toolbar.setTitle(toolbarTitle);
        setSupportActionBar(toolbar);
        if(currentView!=R.id.settings) {
            TasksFragment tasksFragment = createTasksFragment(start, end, currentView);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, tasksFragment).commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt(Constants.CURRENT_VIEW_BUNDLE, currentView);
        savedInstanceState.putSerializable(Constants.DATE_START_BUNDLE, start);
        savedInstanceState.putSerializable(Constants.DATE_END_BUNDLE, end);
    }

    /**
     * Setup drawer.
     */
    private void setupDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.tasks_all:
                        start=null;
                        end=null;
                        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.tasks_all));
                        currentView = R.id.tasks_all;
                        menuItem.setChecked(true);
                        break;
                    case R.id.tasks_today:
                        start = DateUtils.truncate(new Date(), Calendar.DATE);
                        end = DateUtils.addDays(start, 1);
                        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.tasks_today));
                        currentView = R.id.tasks_today;
                        menuItem.setChecked(true);
                        break;
                    case R.id.tasks_week:
                        start = DateUtils.truncate(new Date(), Calendar.DATE);
                        end = DateUtils.addWeeks(start, 1);
                        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.tasks_week));
                        currentView = R.id.tasks_week;
                        menuItem.setChecked(true);
                        break;
                    case R.id.tasks_month:
                        start = DateUtils.truncate(new Date(), Calendar.DATE);
                        end = DateUtils.addMonths(start, 1);
                        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.tasks_month));
                        currentView = R.id.tasks_month;
                        menuItem.setChecked(true);
                        break;
                    case R.id.settings :
                        currentView = R.id.settings;
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                        menuItem.setChecked(false);
                        break;
                    case R.id.tasks_late:
                        start = null;
                        end = null;
                        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.tasks_late));
                        currentView = R.id.tasks_late;
                        menuItem.setChecked(true);

                }
                Fragment fragment = createTasksFragment(start, end, currentView);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
                drawerLayout.closeDrawers();
                return true;
            }

        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    /**
     *
     * @param start
     * @param end
     * @param viewType
     * @return a {@link TasksFragment} based on start, end and viewType
     */
    private TasksFragment createTasksFragment(Date start, Date end, int viewType){
        Bundle bundle = new Bundle();
        bundle.putSerializable(TasksFragment.START, start);
        bundle.putSerializable(TasksFragment.END, end);
        bundle.putInt(TasksFragment.VIEW_TYPE, viewType);
        TasksFragment tasksFragment = new TasksFragment();
        tasksFragment.setArguments(bundle);
        return tasksFragment;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Convert the preference of default view ({@link String}) into a int
     *
     * @return an int representing the current view
     */
    private int getDefaultView(){
        String view = PreferenceManager.getDefaultSharedPreferences(this).getString(getResources().getString(R.string.preferences_default_view_key),getResources().getString(R.string.tasks_today));
        if(view.equals(getResources().getString(R.string.tasks_today))){
            return R.id.tasks_today;
        }
        else if(view.equals(getResources().getString(R.string.tasks_week))){
            return R.id.tasks_week;
        }else if(view.equals(getResources().getString(R.string.tasks_month))){
            return R.id.tasks_month;
        }else if(view.equals(getResources().getString(R.string.tasks_late))){
            return R.id.tasks_late;
        }else  if(view.equals(getResources().getString(R.string.tasks_all))) {
            return R.id.tasks_all;
        }
        throw new IllegalStateException();
    }

    /**
     * Set the start and end based on the currentView
     */
    private void setStartEnd(){
        switch (currentView){
            case R.id.tasks_all:
            case R.id.tasks_late :
                start=null;
                end=null;
                break;
            case R.id.tasks_today:
                start = DateUtils.truncate(new Date(), Calendar.DATE);
                end = DateUtils.addDays(start, 1);
                break;
            case R.id.tasks_week:
                start = DateUtils.truncate(new Date(), Calendar.DATE);
                end = DateUtils.addWeeks(start, 1);
                break;
            case R.id.tasks_month:
                start = DateUtils.truncate(new Date(), Calendar.DATE);
                end = DateUtils.addMonths(start, 1);
                break;
        }
    }
}
