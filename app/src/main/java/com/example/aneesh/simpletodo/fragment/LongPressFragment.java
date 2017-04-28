package com.example.aneesh.simpletodo.fragment;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aneesh.simpletodo.R;

/**
 * Created by Aneesh on 4/28/2017.
 */

public class LongPressFragment extends DialogFragment {

    public LongPressFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below


    }

    public static LongPressFragment newInstance() {
        LongPressFragment frag = new LongPressFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.long_press_layout, container, false);
        return v;
    }
}
