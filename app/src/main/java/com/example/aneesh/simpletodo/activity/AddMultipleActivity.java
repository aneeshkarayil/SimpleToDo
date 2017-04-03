package com.example.aneesh.simpletodo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.aneesh.simpletodo.R;

/**
 * Created by Aneesh on 4/3/2017.
 */

public class AddMultipleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_multiple);

        Toolbar toolbar = (Toolbar)findViewById(R.id.add_multiple_toolbar);
        toolbar.setTitle(R.string.add_multiple_title);
        toolbar.setLogo(R.drawable.ic_arrow_back);
    }

}



