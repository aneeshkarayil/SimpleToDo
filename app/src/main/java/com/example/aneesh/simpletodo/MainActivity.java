package com.example.aneesh.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.aneesh.simpletodo.Utils.TaskUtils;
import com.example.aneesh.simpletodo.activity.EditItemActivity;
import com.example.aneesh.simpletodo.adapter.TasksAdapter;
import com.example.aneesh.simpletodo.model.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static final int EDIT_ACTIVITY_CODE = 100;
    public static final String EDIT_TASK_UUID = "com.aadhyaapps.anothertodo.MainActivity.EDIT_TASK_UUID";
    public static String PARENT_UUID = "com.aadhyaapps.anothertodo.MainActivity.PARENT_UUID";
    public static String TASK_DESCRIPTION = "com.aadhyaapps.anothertodo.MainActivity.TASK_DESCRIPTION";
    private List<Task> tasks;
    private UUID parentUUID;
    private TasksAdapter taskAdapter;
    private TextView parentTaskTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar appBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appBar);

        if (getIntent().getSerializableExtra(MainActivity.PARENT_UUID) != null) {
            this.parentUUID = (UUID) getIntent().getSerializableExtra(MainActivity.PARENT_UUID);
            tasks = TaskUtils.getChildTasks(TaskUtils.generateTasks(), parentUUID);
        } else {
            tasks = TaskUtils.getParentTasks(TaskUtils.generateTasks());
            this.parentUUID = null;
        }

        parentTaskTextView = (TextView) findViewById(R.id.parent_text);
        parentTaskTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task parentTask = TaskUtils.getTaskForUUID(parentUUID);
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra(MainActivity.TASK_DESCRIPTION, parentTask.getDescription());
                intent.putExtra(MainActivity.EDIT_TASK_UUID, parentTask.getTaskId());
                startActivityForResult(intent, EDIT_ACTIVITY_CODE);
            }
        });

        if (parentUUID == null) {
            parentTaskTextView.setVisibility(View.GONE);
        } else {
            parentTaskTextView.setVisibility(View.VISIBLE);
            Task parentTask = TaskUtils.getTaskForUUID(parentUUID);
            parentTaskTextView.setText(parentTask.getDescription());
        }


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        taskAdapter = new TasksAdapter(MainActivity.this, tasks);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar bottomToolbar = (Toolbar) findViewById(R.id.bottomBar);
        bottomToolbar.inflateMenu(R.menu.menu_bottom);
        bottomToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_done_all:
                        Toast.makeText(MainActivity.this, "Done all", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_move:
                        Toast.makeText(MainActivity.this, "Move", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_delete:
                        deleteCheckedItems();
                        return true;
                    case R.id.menu_share:
                        Toast.makeText(MainActivity.this, "Share", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_sort:
                        Toast.makeText(MainActivity.this, "Sort", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_add_multiple:
                        Toast.makeText(MainActivity.this, "Add Multiple", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_export:
                        Toast.makeText(MainActivity.this, "Export", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_feedback:
                        Toast.makeText(MainActivity.this, "Feedback", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_settings:
                        Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        return true;
                }


                return false;
            }
        });

        final EditText editText = (EditText) findViewById(R.id.add_item_text);

        ImageView addItemView = (ImageView) findViewById(R.id.add_item_click);
        addItemView.setOnClickListener(new View.OnClickListener() {

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

    private void deleteCheckedItems() {
        final List<Task> checkedItems = new ArrayList<>();
        final List<Task> childItems = new ArrayList<>();
        for (Task task: tasks)
        {
            if (task.isDone())
            {
                checkedItems.add(task);
            }
        }

        if (checkedItems.size() == 0)
        {
            Toast.makeText(MainActivity.this, "No checked items to delete in the list", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Task checkedItem : checkedItems)
        {
            childItems.addAll(TaskUtils.getChildTasks(TaskUtils.generateTasks(), checkedItem.getTaskId()));
        }

        if (childItems.size() == 0)
        {
            new MaterialDialog.Builder(this)
                    .title(R.string.delete_dialog_title)
                    .content("Are you sure you want to delete "+checkedItems.size()+" checked items?")
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            deleteFromList(checkedItems, childItems);
                        }
                    })
                    .show();
        }
        else
        {
            new MaterialDialog.Builder(this)
                    .title(R.string.delete_dialog_title)
                    .content("Are you sure you want to delete "+checkedItems.size()+" checked items and " + childItems.size()+" sub-items?")
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            deleteFromList(checkedItems, childItems);
                        }
                    })
                    .show();
        }

    }

    private void deleteFromList(List<Task> checkedItems, List<Task> childItems) {

        List<Task> newTaskList = TaskUtils.generateTasks();

        Iterator<Task> iterator = newTaskList.iterator();
        while (iterator.hasNext())
        {
            Task task = iterator.next();
            if (checkedItems.contains(task) || childItems.contains(task))
            {
                iterator.remove();
            }
        }

        swapAdapterData(newTaskList);

        Toast.makeText(MainActivity.this, "Deleted "+checkedItems.size() + " checked items and " + childItems.size() + " child items", Toast.LENGTH_SHORT).show();

    }

    private void swapAdapterData(List<Task> newTaskList) {
        if (parentUUID == null) {
            taskAdapter.swapData(TaskUtils.getParentTasks(newTaskList));
        } else {
            taskAdapter.swapData(TaskUtils.getChildTasks(newTaskList, parentUUID));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UUID taskUUID = (UUID) data.getSerializableExtra(EditItemActivity.EDIT_ITEM_UUID);
        String description = data.getExtras().get(EditItemActivity.EDIT_ITEM_DESCRIPTION).toString();

        if (requestCode == EDIT_ACTIVITY_CODE && resultCode == RESULT_OK) {
            List<Task> updatedTaskList = TaskUtils.generateTasks();
            for (Task task : updatedTaskList) {
                if (task.getTaskId() != null && taskUUID != null && task.getTaskId().equals(taskUUID)) {
                    task.setDescription(description);
                    break;
                }
            }
            parentTaskTextView.setText(description);
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