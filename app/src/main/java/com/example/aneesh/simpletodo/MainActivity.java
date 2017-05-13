package com.example.aneesh.simpletodo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.aneesh.simpletodo.Utils.TaskShareFormatter;
import com.example.aneesh.simpletodo.Utils.TaskUtils;
import com.example.aneesh.simpletodo.activity.AddMultipleActivity;
import com.example.aneesh.simpletodo.activity.EditItemActivity;
import com.example.aneesh.simpletodo.activity.SettingsActivity;
import com.example.aneesh.simpletodo.adapter.TasksAdapter;
import com.example.aneesh.simpletodo.fragment.MoveFragment;
import com.example.aneesh.simpletodo.fragment.SortFragment;
import com.example.aneesh.simpletodo.model.SortSetting;
import com.example.aneesh.simpletodo.model.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static com.example.aneesh.simpletodo.R.id.toolbar;
import static com.example.aneesh.simpletodo.Utils.TaskShareFormatter.getFormattedTask;
import static com.example.aneesh.simpletodo.Utils.TaskUtils.taskList;

public class MainActivity extends AppCompatActivity  implements SortFragment.SortFragmentListener{

    public static final int EDIT_ACTIVITY_CODE = 100;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 101;
    public static final int SETTING_ACTIVITY_REQUEST_CODE = 102;
    public static final int ADD_MULTIPLE_ACTIVITY_REQUEST_CODE = 103;
    public static final String EDIT_TASK_UUID = "com.aadhyaapps.anothertodo.MainActivity.EDIT_TASK_UUID";
    public static String PARENT_UUID = "com.aadhyaapps.anothertodo.MainActivity.PARENT_UUID";
    public static String TASK_DESCRIPTION = "com.aadhyaapps.anothertodo.MainActivity.TASK_DESCRIPTION";
    private List<Task> tasks;
    private UUID parentUUID;
    private TasksAdapter taskAdapter;
    private TextView parentTaskTextView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //writeJsonFile();
        initiateSortSetting();
        initiateTaskList();
        if (getFileReader() == null)
        {
            writeJsonFile();
        }



        Toolbar appBar = (Toolbar) findViewById(toolbar);
        setSupportActionBar(appBar);

        if (getIntent().getSerializableExtra(MainActivity.PARENT_UUID) != null) {
            this.parentUUID = (UUID) getIntent().getSerializableExtra(MainActivity.PARENT_UUID);
            tasks = TaskUtils.getChildTasks(TaskUtils.generateTasks(), parentUUID);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home_black);
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

        final Toolbar bottomToolbar = (Toolbar) findViewById(R.id.bottomBar);
        bottomToolbar.inflateMenu(R.menu.menu_bottom);
        bottomToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_done_all:
                        showCheckAllPopUpMenu(bottomToolbar);
                        return true;
                    case R.id.menu_move:
                        showMoveFragment(parentUUID);
                        return true;
                    case R.id.menu_delete:
                        deleteCheckedItems();
                        return true;
                    case R.id.menu_share:
                        shareIntent(tasks);
                        return true;
                    case R.id.menu_sort:
                        showSort();
                        return true;
                    case R.id.menu_add_multiple:
                        //Toast.makeText(MainActivity.this, "Add Multiple", Toast.LENGTH_SHORT).show();
                        showAddItemActivity();
                        return true;
                    case R.id.menu_export:
                        //Toast.makeText(MainActivity.this, "Export", Toast.LENGTH_SHORT).show();
                        formatAndExportFile();
                        return true;
                    case R.id.menu_feedback:
                        sendEmail();
                        return true;
                    case R.id.menu_settings:
                        showSettingsScreen();
                        return true;
                }


                return false;
            }
        });

       this.editText = (EditText) findViewById(R.id.add_item_text);

        ImageView addItemView = (ImageView) findViewById(R.id.add_item_click);
        addItemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Adding " + editText.getText().toString(), Toast.LENGTH_SHORT).show();

                if (editText.getText().toString().length() > 0)
                {
                    Task newTask = new Task(editText.getText().toString(), parentUUID);
                    List<Task> newTaskList = new ArrayList<Task>(TaskUtils.generateTasks());
                    newTaskList.add(newTask);
                    TaskUtils.generateTasks().add(newTask);

                    swapAdapterData(newTaskList, true);

                    editText.setText("");
                }
            }
        });

        ImageView speechToTextView = (ImageView)findViewById(R.id.speech_view);
        speechToTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Invoked speech to text ", Toast.LENGTH_SHORT).show();
                startVoiceRecognitionActivity();
            }
        });

    }

    private void showAddItemActivity() {
        Intent intent = new Intent(MainActivity.this, AddMultipleActivity.class);
        startActivityForResult(intent, ADD_MULTIPLE_ACTIVITY_REQUEST_CODE);
    }

    private void formatAndExportFile() {
        String formattedTasks = TaskShareFormatter.getFormattedTask(TaskUtils.generateTasks());

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getFileOutputStream("TaskFile.txt")));
            writer.write(formattedTasks);
            writer.close();

            Toast.makeText(this, "File written to " + getFilesDir().getAbsolutePath()+"TaskFile.txt", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(this, "Error writing the report file", Toast.LENGTH_SHORT).show();
        }

    }

    private void initiateSortSetting() {
        if (getSettingFileReader() != null)
        {
            try {
                String json = TaskUtils.readJSONFromFile(getSettingFileReader());
                SortSetting sortSetting = SortSetting.convertFromJSON(json);
                //Toast.makeText(this, "Read from sort setting json file: " + SortSetting.getInstance().getSortSetting(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "No input json found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showSort() {
        SortFragment sortFragment = SortFragment.newInstance();
        sortFragment.show(getSupportFragmentManager(), "Sort");
    }

    public void writeJsonFile() {
        try {
            TaskUtils.writeJSONToFile(TaskUtils.convertToJSON(TaskUtils.generateTasks()),getFileOutputStream("tasksFile.txt"));
            //Toast.makeText(this, "Written Json to file", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error trying to write to file", Toast.LENGTH_SHORT).show();
        }
    }

    private void initiateTaskList() {
        if (taskList == null && getFileReader() != null)
        {
            try {
                String json = TaskUtils.readJSONFromFile(getFileReader());
                List<Task> taskList = TaskUtils.convertFromJSON(json);
                //Toast.makeText(this, "Read from json file", Toast.LENGTH_SHORT).show();
                TaskUtils.setTaskList(taskList);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "No input json found", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public FileOutputStream getFileOutputStream(String fileName) throws FileNotFoundException {
        FileOutputStream fos = openFileOutput(fileName, MODE_WORLD_WRITEABLE);
        return fos;
    }

    public BufferedReader getFileReader()  {
        BufferedReader input = null;
        try {
            input = new BufferedReader(
                    new InputStreamReader(openFileInput("tasksFile.txt")));
        } catch (FileNotFoundException e) {
            return null;
        }
        return input;
    }

    public BufferedReader getSettingFileReader()  {
        BufferedReader input = null;
        try {
            input = new BufferedReader(
                    new InputStreamReader(openFileInput("sortSetting.txt")));
        } catch (FileNotFoundException e) {
            return null;
        }
        return input;
    }

    private void showMoveFragment(UUID parentUUID) {

        List<UUID> checkedTaskIds = getCheckedTaskIds();

        if (checkedTaskIds.size() == 0)
        {
            Toast.makeText(this, "There are no checked items in this list", Toast.LENGTH_SHORT).show();
            return;
        }

        FragmentManager fm = getSupportFragmentManager();
        MoveFragment moveFragment = MoveFragment.newInstance(parentUUID, checkedTaskIds);
        moveFragment.show(fm, "fragment_edit_name");

    }

    private List<UUID> getCheckedTaskIds() {

        List<UUID> checkedTasks = new ArrayList<>();

        for (Task task: tasks)
        {
            if (task.isDone())
            {
               checkedTasks.add(task.getTaskId());
            }
        }

        return checkedTasks;
    }

    private void showSettingsScreen() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        //intent.putExtra(MainActivity.TASK_DESCRIPTION, parentTask.getDescription());
        //intent.putExtra(MainActivity.EDIT_TASK_UUID, parentTask.getTaskId());
        startActivityForResult(intent, SETTING_ACTIVITY_REQUEST_CODE);
    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak...");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                taskAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                taskAdapter.filter(newText);
                return true;
            }
        });

        return true;
    }


    private void sendEmail() {
        String uriText =
                "mailto:aneeshkarayil@gmail.com" +
                        "?subject=" + Uri.encode("Feedback on your app") +
                        "&body=" + Uri.encode("some text here");

        Uri uri = Uri.parse(uriText);

        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(uri);
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(sendIntent, "Send email"));
        }
    }

    private void shareIntent(List<Task> tasks) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getFormattedTask(tasks));
        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No apps have not been installed to share", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCheckAllPopUpMenu(Toolbar bottomToolbar) {
        PopupMenu popup = new PopupMenu(this, bottomToolbar);
        popup.getMenuInflater().inflate(R.menu.check_all_menu, popup.getMenu());

        popup.getMenu().findItem(R.id.menu_check_all).setVisible(false);
        popup.getMenu().findItem(R.id.menu_check_all_sub_items).setVisible(false);
        popup.getMenu().findItem(R.id.menu_uncheck_all).setVisible(false);
        popup.getMenu().findItem(R.id.menu_uncheck_all_sub_items).setVisible(false);

        setUpCheckAllPopupMenu(popup);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                startActivityAfterCleanup(MainActivity.class);
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private void startActivityAfterCleanup(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setUpCheckAllPopupMenu(PopupMenu popup) {

        final List<Task> mainTaskList = new ArrayList<>(this.tasks);
        final List<Task> childTaskList = new ArrayList<>();

        for (Task parentTask: mainTaskList)
        {
            childTaskList.addAll(TaskUtils.getChildTasks(TaskUtils.generateTasks(), parentTask.getTaskId()));
        }

        //Main task checks
        boolean enableUncheckMainTasks = false;
        boolean enableCheckMainTasks = false;
        for (Task task: mainTaskList)
        {
            if (task.isDone())
            {
                enableUncheckMainTasks = true;
            }
            else
            {
                enableCheckMainTasks = true;
            }

            if (enableUncheckMainTasks && enableCheckMainTasks)
            {
                break;
            }
        }

        //Sub-tasks
        //Main task checks
        boolean enableUncheckSubTasks = false;
        boolean enableCheckSubTasks = false;

        for (Task task: childTaskList)
        {
            if (task.isDone())
            {
                enableUncheckSubTasks = true;
            }
            else
            {
                enableCheckSubTasks = true;
            }

            if (enableUncheckSubTasks && enableCheckSubTasks)
            {
                break;
            }
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_check_all:
                        markAsDone(mainTaskList, true);
                        refreshDataSet();
                        Toast.makeText(MainActivity.this, "Menu Check all", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_check_all_sub_items:
                        markAsDone(mainTaskList, true);
                        markAsDone(childTaskList, true);
                        refreshDataSet();
                        Toast.makeText(MainActivity.this, "Menu Check all subitems", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_uncheck_all:
                        markAsDone(mainTaskList, false);
                        refreshDataSet();
                        Toast.makeText(MainActivity.this, "Menu Uncheck all", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_uncheck_all_sub_items:
                        markAsDone(mainTaskList, false);
                        markAsDone(childTaskList, false);
                        refreshDataSet();
                        Toast.makeText(MainActivity.this, "Menu Uncheck all subitems", Toast.LENGTH_SHORT).show();
                        return true;

                }
                return false;
            }
        });


        if (enableCheckMainTasks)
            popup.getMenu().findItem(R.id.menu_check_all).setVisible(true);
        if (enableUncheckMainTasks)
            popup.getMenu().findItem(R.id.menu_uncheck_all).setVisible(true);
        if (enableCheckSubTasks)
            popup.getMenu().findItem(R.id.menu_check_all_sub_items).setVisible(true);
        if (enableUncheckSubTasks)
            popup.getMenu().findItem(R.id.menu_uncheck_all_sub_items).setVisible(true);

        popup.show();

    }

    private void markAsDone(List<Task> mainTaskList, boolean isDone) {
        for (Task task: mainTaskList)
        {
            task.setDone(isDone);
        }
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

    public void refreshDataSet()
    {
        List<Task> updatedTaskList = TaskUtils.generateTasks();
        this.swapAdapterData(updatedTaskList, false);
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

        swapAdapterData(newTaskList, true);

        Toast.makeText(MainActivity.this, "Deleted "+checkedItems.size() + " checked items and " + childItems.size() + " child items", Toast.LENGTH_SHORT).show();

    }

    public void swapAdapterData(List<Task> newTaskList, boolean dataChanged) {
        if (parentUUID == null) {
            taskAdapter.swapData(TaskUtils.getParentTasks(newTaskList));
        } else {
            taskAdapter.swapData(TaskUtils.getChildTasks(newTaskList, parentUUID));
        }

        if (dataChanged)
        {
            writeJsonFile();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_ACTIVITY_CODE && resultCode == RESULT_OK) {
            UUID taskUUID = (UUID) data.getSerializableExtra(EditItemActivity.EDIT_ITEM_UUID);
            String description = data.getExtras().get(EditItemActivity.EDIT_ITEM_DESCRIPTION).toString();
            List<Task> updatedTaskList = TaskUtils.generateTasks();
            for (Task task : updatedTaskList) {
                if (task.getTaskId() != null && taskUUID != null && task.getTaskId().equals(taskUUID)) {
                    task.setDescription(description);
                    break;
                }
            }
            parentTaskTextView.setText(description);
            writeJsonFile();
        }

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if (matches.size() > 0)
            {
                this.editText.setText(matches.get(0));
            }
            else
            {
                Toast.makeText(this, "Could not recognize the voice.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Task> updatedTaskList = TaskUtils.generateTasks();
        this.swapAdapterData(updatedTaskList, false);
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        List<Task> updatedTaskList = TaskUtils.generateTasks();
        this.swapAdapterData(updatedTaskList, false);
    }

    @Override
    public void onSortItemSelected() {
        List<Task> updatedTaskList = TaskUtils.generateTasks();
        this.swapAdapterData(updatedTaskList, false);
    }
}