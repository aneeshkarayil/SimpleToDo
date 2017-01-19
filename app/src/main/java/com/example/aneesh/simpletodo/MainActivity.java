package com.example.aneesh.simpletodo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.aneesh.simpletodo.Utils.TaskUtils;
import com.example.aneesh.simpletodo.adapter.TasksAdapter;
import com.example.aneesh.simpletodo.model.Task;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static String PARENT_UUID = "com.example.aneesh.simpletodo.MainActivity.PARENT_UUID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar appBar = (Toolbar)findViewById(R.id.appBar);
        setSupportActionBar(appBar);

        RecyclerView taskView = (RecyclerView)findViewById(R.id.taskList);
        List<Task> allTasks = TaskUtils.createTestTasks();


        List<Task> tasks = null ;

        UUID parentUUID = (UUID) getIntent().getSerializableExtra(PARENT_UUID);

        if (parentUUID == null)
        {
            tasks = TaskUtils.getParentTasks(allTasks);
        }
        else
        {
            tasks = TaskUtils.getChildTasks(allTasks, parentUUID);
        }

        taskView.setAdapter(new TasksAdapter(this, tasks));
        taskView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar bottomToolBar = (Toolbar)findViewById(R.id.bottom_toolbar);
        bottomToolBar.inflateMenu(R.menu.menu_bottom);

        bottomToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return true;
    }
}
