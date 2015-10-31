package com.hashicode.simpletodo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.hashicode.simpletodo.R;
import com.hashicode.simpletodo.fragment.SettingsFragment;

/**
 * Created by takahashi on 3/10/16.
 */
public class SettingsActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction()
                .replace(R.id.settings_content, new SettingsFragment())
                .commit();
    }
}
