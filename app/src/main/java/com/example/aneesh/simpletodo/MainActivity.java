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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aneesh.simpletodo.Utils.TaskUtils;
import com.example.aneesh.simpletodo.adapter.TasksAdapter;
import com.example.aneesh.simpletodo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static String PARENT_UUID = "com.aadhyaapps.anothertodo.MainActivity.PARENT_UUID";
    private List<Task> tasks;
    private UUID parentUUID;
    private TasksAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar appBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(appBar);

        if (getIntent().getSerializableExtra(MainActivity.PARENT_UUID) != null)
        {
            this.parentUUID = (UUID)getIntent().getSerializableExtra(MainActivity.PARENT_UUID);
            tasks = TaskUtils.getChildTasks(TaskUtils.generateTasks(), parentUUID);
        }
        else
        {
            tasks  = TaskUtils.getParentTasks(TaskUtils.generateTasks());
            this.parentUUID = null;
        }


        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        taskAdapter = new TasksAdapter(MainActivity.this, tasks);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar bottomToolbar = (Toolbar)findViewById(R.id.bottomBar);
        bottomToolbar.inflateMenu(R.menu.menu_bottom);

        final EditText editText = (EditText)findViewById(R.id.add_item_text);

        ImageView addItemView = (ImageView)findViewById(R.id.add_item_click);
        addItemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Adding " + editText.getText().toString(), Toast.LENGTH_SHORT).show();

                Task newTask = new Task(editText.getText().toString(), parentUUID);
                List<Task> newTaskList = new ArrayList<Task>(TaskUtils.generateTasks());
                newTaskList.add(newTask);
                TaskUtils.generateTasks().add(newTask);

                swapAdapterData(newTaskList);

                editText.setText("");
            }
        });

    }

    private void swapAdapterData(List<Task> newTaskList) {
        if (parentUUID == null)
        {
            taskAdapter.swapData(TaskUtils.getParentTasks(newTaskList));
        }
        else
        {
            taskAdapter.swapData(TaskUtils.getChildTasks(newTaskList, parentUUID));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Task> updatedTaskList = TaskUtils.generateTasks();
        this.swapAdapterData(updatedTaskList);
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        List<Task> updatedTaskList = TaskUtils.generateTasks();
        this.swapAdapterData(updatedTaskList);
    }

}