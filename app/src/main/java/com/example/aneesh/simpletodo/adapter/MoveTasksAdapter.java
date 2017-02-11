package com.example.aneesh.simpletodo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aneesh.simpletodo.MainActivity;
import com.example.aneesh.simpletodo.R;
import com.example.aneesh.simpletodo.Utils.TaskUtils;
import com.example.aneesh.simpletodo.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aneesh on 2/11/2017.
 */

public class MoveTasksAdapter extends RecyclerView.Adapter<MoveTasksAdapter.ViewHolder> {

    private List<Task> taskList;
    private Context context;
    private List<Task> taskListCopy;

    public MoveTasksAdapter(Context context, List<Task> taskList)
    {
        this.context = context;
        this.taskList = taskList;
        this.taskListCopy = new ArrayList<Task>(taskList);
    }

    @Override
    public MoveTasksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.move_task_item, parent, false);
        MoveTasksAdapter.ViewHolder viewHolder = new MoveTasksAdapter.ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MoveTasksAdapter.ViewHolder holder, int position) {

        final Task task = taskList.get(position);

        TextView textView = holder.mItemTextView;
        Button progressButton = holder.mItemButton;

        textView.setText(task.getDescription());

        //get the children for the current task
        List<Task> childTasks = TaskUtils.getChildTasks(TaskUtils.generateTasks(), task.getTaskId());

        if (childTasks.size() == 0)
        {
            progressButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            progressButton.setVisibility(View.VISIBLE);
            progressButton.setText(childTasks.size()+"");
        }


    }

    private boolean checkAllSubTasksDone() {
        boolean allDone = true;

        for (Task task : taskList)
        {
            if (!task.isDone())
            {
                allDone = false;
                break;
            }
        }

        return allDone;



    }

    public void swapData(List<Task> newData)
    {
        this.taskList.clear();
        this.taskList.addAll(newData);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Button mItemButton;
        public TextView mItemTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mItemButton = (Button)itemView.findViewById(R.id.move_item_progress);
            mItemTextView = (TextView)itemView.findViewById(R.id.move_item_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) {
                Task task = taskList.get(position);
//                Intent intent = new Intent(context, MainActivity.class);
//                intent.putExtra(MainActivity.PARENT_UUID, task.getTaskId());
//                ((Activity)context).startActivity(intent);
                swapData(TaskUtils.getChildTasks(TaskUtils.generateTasks(),task.getTaskId()));
            }
        }
    }
}
