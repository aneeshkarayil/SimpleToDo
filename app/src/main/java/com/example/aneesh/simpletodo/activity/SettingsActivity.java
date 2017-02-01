package com.example.aneesh.simpletodo.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.aneesh.simpletodo.R;
import com.example.aneesh.simpletodo.fragment.SettingsFragment;

/**
 * Created by Aneesh on 1/30/2017.
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        toolbar.setTitle(R.string.settings);
        toolbar.setLogo(R.drawable.ic_arrow_back);

    }


}
