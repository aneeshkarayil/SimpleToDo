package com.example.aneesh.simpletodo.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aneesh.simpletodo.MainActivity;
import com.example.aneesh.simpletodo.R;
import com.example.aneesh.simpletodo.Utils.TaskUtils;
import com.example.aneesh.simpletodo.adapter.TasksAdapter;

/**
 * Created by Aneesh on 2/4/2017.
 */

public class MoveFragment extends DialogFragment {

    public MoveFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button mainListButton = (Button)getView().findViewById(R.id.fragment_main_list_button);
        mainListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Move To Main List", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static MoveFragment newInstance(String title) {
        MoveFragment frag = new MoveFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        // Use the Builder class for convenient dialog construction
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        builder.setTitle(R.string.move)
//                .setView(inflater.inflate(R.layout.move_fragment, null))
//                .setOnItemSelectedListener();
//
////                .setPositiveButton(R.string.main_list, new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog, int id) {
////                        // FIRE ZE MISSILES!
////                    }
////                })
////                .setNeutralButton(R.string.move_to_main_list, new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog, int id) {
////                        // FIRE ZE MISSILES!
////                    }
////                })
////                .setNegativeButton(R.string.level_up, new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog, int id) {
////                        // User cancelled the dialog
////                    }
////                });
//        // Create the AlertDialog object and return it
//        return builder.create();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.move_fragment, container, false);


        // Watch for button clicks.
        Button mainListButton = (Button)v.findViewById(R.id.fragment_main_list_button);
        mainListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Move To Main List", Toast.LENGTH_SHORT).show();
            }
        });

        final RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.fragment_recycler_view);
        TasksAdapter taskAdapter = new TasksAdapter(getActivity(), TaskUtils.getParentTasks(TaskUtils.generateTasks()));
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

}