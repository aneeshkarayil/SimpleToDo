package com.example.aneesh.simpletodo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.aneesh.simpletodo.MainActivity;
import com.example.aneesh.simpletodo.R;
import com.example.aneesh.simpletodo.views.ProgressBarView;

/**
 * Created by Aneesh on 6/6/2017.
 */

public class ProgressBarActivity extends AppCompatActivity {

    private int totalTasks;
    private int completedTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_bar);

        if (getIntent().getSerializableExtra(MainActivity.TOTAL_TASKS) != null)
        {
            this.totalTasks = (int)getIntent().getIntExtra(MainActivity.TOTAL_TASKS, 0);
            this.completedTasks = (int)getIntent().getIntExtra(MainActivity.COMPLETED_TASKS, 0);
        }

        ProgressBarView progressBarView = (ProgressBarView)findViewById(R.id.progress_view);
        progressBarView.setCounts(totalTasks, completedTasks);
    }
}
