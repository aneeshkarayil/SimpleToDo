package com.example.aneesh.simpletodo.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.aneesh.simpletodo.R;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by Aneesh on 2/15/2017.
 */

public class SortFragment extends DialogFragment {

    RadioButton alphabeticalRadioButton;
    RadioButton manualRadioButton;
    RadioButton newestFirstRadioButton;
    RadioButton newestLastRadioButton;

    Button okButton;
    Button cancelButton;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.sort_layout, container, false);

          this.alphabeticalRadioButton = (RadioButton)v.findViewById(R.id.sort_alphabetical);
          this.manualRadioButton = (RadioButton)v.findViewById(R.id.sort_manual);
          this.newestFirstRadioButton = (RadioButton)v.findViewById(R.id.sort_newest_first);
          this.newestLastRadioButton = (RadioButton)v.findViewById(R.id.sort_newest_last);

        this.okButton = (Button)v.findViewById(R.id.sort_ok);
        this.cancelButton = (Button)v.findViewById(R.id.sort_cancel);

        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortFragment.this.getDialog().cancel();
            }
        });

        this.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alphabeticalRadioButton.isChecked())
                        {
                            Toast.makeText(getActivity(), "Alphabetical", Toast.LENGTH_SHORT).show();
                        }
                        else if (manualRadioButton.isChecked())
                        {
                            Toast.makeText(getActivity(), "Manual", Toast.LENGTH_SHORT).show();
                        }
                        else if (newestFirstRadioButton.isChecked())
                        {
                            Toast.makeText(getActivity(), "Newest First", Toast.LENGTH_SHORT).show();
                        }
                        else if (newestLastRadioButton.isChecked())
                        {
                            Toast.makeText(getActivity(), "Newest Last", Toast.LENGTH_SHORT).show();
                        }

            }
        });

        return v;
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        // Get the layout inflater
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        this.alphabeticalRadioButton = (RadioButton)getView().findViewById(R.id.sort_alphabetical);
//        this.manualRadioButton = (RadioButton)getView().findViewById(R.id.sort_manual);
//        this.newestFirstRadioButton = (RadioButton)getView().findViewById(R.id.sort_newest_first);
//        this.newestLastRadioButton = (RadioButton)getView().findViewById(R.id.sort_newest_last);
//
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        builder.setView(inflater.inflate(R.layout.sort_layout, null))
//                // Add action buttons
//                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//
//
//                        if (alphabeticalRadioButton.isChecked())
//                        {
//                            Toast.makeText(getActivity(), "Alphabetical", Toast.LENGTH_SHORT).show();
//                        }
//                        else if (manualRadioButton.isChecked())
//                        {
//                            Toast.makeText(getActivity(), "Alphabetical", Toast.LENGTH_SHORT).show();
//                        }
//                        else if (newestFirstRadioButton.isChecked())
//                        {
//                            Toast.makeText(getActivity(), "Alphabetical", Toast.LENGTH_SHORT).show();
//                        }
//                        else if (newestLastRadioButton.isChecked())
//                        {
//                            Toast.makeText(getActivity(), "Alphabetical", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                })
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        SortFragment.this.getDialog().cancel();
//                    }
//                });
//        return builder.create();
//    }
}
