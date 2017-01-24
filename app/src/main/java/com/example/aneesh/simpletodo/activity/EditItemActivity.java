package com.example.aneesh.simpletodo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.example.aneesh.simpletodo.MainActivity;
import com.example.aneesh.simpletodo.R;
import com.example.aneesh.simpletodo.Utils.TaskUtils;

import java.util.UUID;

/**
 * Created by Aneesh on 1/22/2017.
 */

public class EditItemActivity  extends AppCompatActivity {

    public static final String EDIT_ITEM_DESCRIPTION = "com.example.aneesh.simpletodo.activity.EditItemActivity.EDIT_ITEM_DESCRIPTION";
    public static final String EDIT_ITEM_UUID = "com.example.aneesh.simpletodo.activity.EditItemActivity.EDIT_ITEM_UUID";
    private String taskDescription;
    private EditText editText;
    private UUID taskUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);

        Toolbar toolbar = (Toolbar)findViewById(R.id.edit_item_toolbar);
        toolbar.setTitle(R.string.edit_text);
        toolbar.setLogo(R.drawable.ic_arrow_back);

        if (getIntent().getSerializableExtra(MainActivity.TASK_DESCRIPTION) != null)
        {
            this.taskDescription = (String)getIntent().getSerializableExtra(MainActivity.TASK_DESCRIPTION);
            this.editText = (EditText) findViewById(R.id.edit_item);
            this.taskUUID = (UUID) getIntent().getSerializableExtra(MainActivity.EDIT_TASK_UUID);
            editText.setText(taskDescription);
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        intent.putExtra(EDIT_ITEM_DESCRIPTION, this.editText.getText());
        intent.putExtra(EDIT_ITEM_UUID, this.taskUUID);
        setResult(RESULT_OK, intent);
        finish();
    }
}
