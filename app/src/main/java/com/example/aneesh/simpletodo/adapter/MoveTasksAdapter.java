package com.example.aneesh.simpletodo.adapter;

import android.content.Context;
import android.view.View;

import com.example.aneesh.simpletodo.model.Task;

import java.util.List;

/**
 * Created by Aneesh on 2/11/2017.
 */

public class MoveTasksAdapter extends TasksAdapter {

    public MoveTasksAdapter(Context context, List<Task> taskList) {
        super(context, taskList);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.mItemCheckbox.setVisibility(View.GONE);
    }
}
