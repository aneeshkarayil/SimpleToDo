package com.example.aneesh.simpletodo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aneesh.simpletodo.MainActivity;
import com.example.aneesh.simpletodo.R;
import com.example.aneesh.simpletodo.Utils.TaskUtils;
import com.example.aneesh.simpletodo.adapter.MoveTasksAdapter;
import com.example.aneesh.simpletodo.model.Task;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by Aneesh on 2/4/2017.
 */

public class MoveFragment extends DialogFragment {
    UUID parentId;
    List<UUID> checkedTasks;
    // Watch for button clicks.
    Button mainListButton;
    Button moveToMainListButton;
    Button levelUpButton;

    public MoveFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public void setParentId(UUID parentId)
    {
        this.parentId = parentId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentId = (UUID) getArguments().getSerializable("parentUUID");
        checkedTasks=(List<UUID>)getArguments().getSerializable("checkedTasks");
    }



    public static MoveFragment newInstance(UUID parentUUID, List<UUID> checkedTaskIds) {
        MoveFragment frag = new MoveFragment();
        Bundle args = new Bundle();
        args.putSerializable("parentUUID", parentUUID);
        args.putSerializable("checkedTasks", (Serializable) checkedTaskIds);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.move_fragment, container, false);
        List<Task> tasks;

        if (parentId != null)
        {
            tasks = TaskUtils.getChildTasks(TaskUtils.generateTasks(), parentId);
        }
        else
        {
            tasks = TaskUtils.getParentTasks(TaskUtils.generateTasks());
        }



        mainListButton = (Button)v.findViewById(R.id.fragment_main_list_button);
        levelUpButton = (Button)v.findViewById(R.id.fragment_level_up_button);
        moveToMainListButton = (Button)v.findViewById(R.id.fragment_move_to_main_list_button);



        enableButtons();

        final Toolbar moveToolbar = (Toolbar) v.findViewById(R.id.move_toolbar);
        moveToolbar.setTitle(R.string.move);

        final RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.fragment_recycler_view);
        final MoveTasksAdapter taskAdapter = new MoveTasksAdapter(getActivity(), tasks, this);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final TextView parentTaskView = (TextView)v.findViewById(R.id.fragment_parent_tv);
        if (parentId == null)
        {
            parentTaskView.setVisibility(View.GONE);
        }
        else
        {
            parentTaskView.setText(TaskUtils.getTaskForUUID(parentId).getDescription());
        }

        levelUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task parentTask = TaskUtils.getTaskForUUID(parentId);
                parentId = parentTask.getParentTaskId();

                if (parentId == null)
                {
                    taskAdapter.swapData(TaskUtils.getParentTasks(TaskUtils.generateTasks()));
                }
                else
                {
                    taskAdapter.swapData(TaskUtils.getChildTasks(TaskUtils.generateTasks(), parentId));
                }

            }
        });

        mainListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task parentTask = TaskUtils.getTaskForUUID(parentId);
                parentId = null;
                taskAdapter.swapData(TaskUtils.getParentTasks(TaskUtils.generateTasks()));
            }
        });

        moveToMainListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (UUID taskUUID: checkedTasks)
                {
                    TaskUtils.getTaskForUUID(taskUUID).setParentTaskId(parentId);
                }
                ((MainActivity)getActivity()).refreshDataSet();

                String message;
                if (checkedTasks.size() == 1)
                {
                    message = "Successfully moved the task "+ TaskUtils.getTaskForUUID(checkedTasks.get(0)).getDescription();
                }
                else
                {
                    message = "Successfully moved " + checkedTasks.size() + " tasks";
                }

                dismiss();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

            }
        });


        return v;
    }

    public void enableButtons() {
        if ((parentId == null && TaskUtils.getTaskForUUID(checkedTasks.get(0)).getParentTaskId() == null) || (TaskUtils.getTaskForUUID(checkedTasks.get(0)).getParentTaskId() != null && parentId!= null && parentId.equals(TaskUtils.getTaskForUUID(checkedTasks.get(0)).getParentTaskId())))
        {
            moveToMainListButton.setEnabled(false);
            Toast.makeText(getActivity(), "Both the destination and target is at same level", Toast.LENGTH_SHORT).show();
        }
        else
        {
            moveToMainListButton.setEnabled(true);
        }
        if (parentId == null)
        {
            moveToMainListButton.setText(R.string.move_to_main_list);
            mainListButton.setEnabled(false);
            levelUpButton.setEnabled(false);
        }
        else
        {
            moveToMainListButton.setText(R.string.move_to_this_list);
            mainListButton.setEnabled(true);
            levelUpButton.setEnabled(true);
        }
    }

}
