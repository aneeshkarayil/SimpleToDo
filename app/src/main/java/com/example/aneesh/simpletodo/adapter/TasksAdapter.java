package com.example.aneesh.simpletodo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
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
import com.example.aneesh.simpletodo.fragment.LongPressFragment;
import com.example.aneesh.simpletodo.model.SortSetting;
import com.example.aneesh.simpletodo.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Aneesh on 1/10/2017.
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private List<Task> taskList;
    private Context context;
    private List<Task> taskListCopy;

    public TasksAdapter(Context context, List<Task> taskList)
    {
        Collections.sort(taskList, SortSetting.getInstance().getComparator());
        this.context = context;
        this.taskList = taskList;
        this.taskListCopy = new ArrayList<Task>(taskList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.task_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    public void filter(String text) {
        taskList.clear();
        if(text.isEmpty()){
            taskList.addAll(taskListCopy);
        } else{
            text = text.toLowerCase();
            for(Task item: taskListCopy){
                if(item.getDescription().toLowerCase().contains(text))
                {
                    taskList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Task task = taskList.get(position);

        TextView textView = holder.mItemTextView;
        Button progressButton = holder.mItemButton;
        CheckBox checkBox = holder.mItemCheckbox;

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    if (isChecked)
                                                    {
                                                        task.setDone(true);
                                                        boolean allSubTaskDone = checkAllSubTasksDone();

                                                        if (allSubTaskDone) {

                                                            Task parentTask = TaskUtils.getTaskForUUID(task.getParentTaskId());
                                                            if (parentTask != null)
                                                            {
                                                                Toast.makeText(context, "All sub-tasks done - marking parent task as done", Toast.LENGTH_SHORT).show();
                                                                parentTask.setDone(true);
                                                            }

                                                        }

                                                        writeJSONToFile();

                                                    }
                                                    else
                                                    {
                                                        task.setDone(false);
                                                        Task parentTask = TaskUtils.getTaskForUUID(task.getParentTaskId());
                                                        if (parentTask != null && parentTask.isDone())
                                                        {
                                                            Toast.makeText(context, "marking parent task as not done", Toast.LENGTH_SHORT).show();
                                                            parentTask.setDone(false);
                                                        }
                                                        writeJSONToFile();

                                                    }
                                                }
                                            }
        );

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

        if (task.isDone())
        {
            checkBox.setChecked(true);
        }
        else
        {
            checkBox.setChecked(false);
        }
    }

    private void writeJSONToFile() {
        if (context instanceof MainActivity)
        {
            ((MainActivity) context).writeJsonFile();
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
        Collections.sort(this.taskList, TaskUtils.getComparator());
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public Button mItemButton;
        public CheckBox mItemCheckbox;
        public TextView mItemTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mItemButton = (Button)itemView.findViewById(R.id.item_progress);
            mItemCheckbox = (CheckBox)itemView.findViewById(R.id.item_check_box);
            mItemTextView = (TextView)itemView.findViewById(R.id.item_description);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) {
                Task task = taskList.get(position);
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(MainActivity.PARENT_UUID, task.getTaskId());
                ((Activity)context).startActivity(intent);
            }
        }

        private void showLongPressedFragment() {
            LongPressFragment longPressFragment = LongPressFragment.newInstance();
            longPressFragment.show(((FragmentActivity)context).getSupportFragmentManager(), "Long");
        }

        @Override
        public boolean onLongClick(View v) {
            showLongPressedFragment();
            return true;
        }
    }
}