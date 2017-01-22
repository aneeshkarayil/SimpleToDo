package com.example.aneesh.simpletodo.activity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);

        Toolbar toolbar = (Toolbar)findViewById(R.id.edit_item_toolbar);
        toolbar.setTitle("Edit");
        toolbar.setLogo(R.drawable.ic_arrow_forward);

        if (getIntent().getSerializableExtra(MainActivity.TASK_DESCRIPTION) != null)
        {
            String taskDescription = (String)getIntent().getSerializableExtra(MainActivity.TASK_DESCRIPTION);
            EditText editText = (EditText) findViewById(R.id.edit_item);
            editText.setText(taskDescription);
        }
    }
}
