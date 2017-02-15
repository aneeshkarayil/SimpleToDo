package com.example.aneesh.simpletodo.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.aneesh.simpletodo.R;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by Aneesh on 2/15/2017.
 */

public class SortFragment extends DialogFragment {

    public SortFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static SortFragment newInstance() {
        SortFragment frag = new SortFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("parentUUID", parentUUID);
//        args.putSerializable("checkedTasks", (Serializable) checkedTaskIds);
//        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.sort_layout, null))
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SortFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
