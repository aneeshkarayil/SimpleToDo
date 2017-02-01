package com.example.aneesh.simpletodo.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.aneesh.simpletodo.R;

/**
 * Created by Aneesh on 2/1/2017.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }
}
