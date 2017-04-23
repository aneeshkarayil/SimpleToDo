package com.example.aneesh.simpletodo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aneesh.simpletodo.R;
import com.example.aneesh.simpletodo.Utils.TaskUtils;
import com.example.aneesh.simpletodo.model.Task;

import java.util.List;

/**
 * Created by Aneesh on 4/3/2017.
 */

public class AddMultipleActivity extends AppCompatActivity {

    private EditText editText;
    private Button saveToMainListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_multiple);

        Toolbar toolbar = (Toolbar)findViewById(R.id.add_multiple_toolbar);
        toolbar.setTitle(R.string.add_multiple_title);
        toolbar.setLogo(R.drawable.ic_arrow_back);

        saveToMainListButton = (Button)findViewById(R.id.add_to_main_list_button);
        editText = (EditText)findViewById(R.id.add_multiple_edit_text);

        saveToMainListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (text.length() == 0)
                {
                    Toast.makeText(AddMultipleActivity.this, "Please paste or type a list before saving", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    List<Task> tasks = TaskUtils.convertMultipleToListItems(text);
                    TaskUtils.generateTasks().addAll(tasks);
                    finish();
                }
            }
        });
    }

}



