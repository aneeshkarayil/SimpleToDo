package com.example.aneesh.simpletodo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aneesh.simpletodo.MainActivity;
import com.example.aneesh.simpletodo.R;
import com.example.aneesh.simpletodo.model.Task;

import java.util.List;

/**
 * Created by Aneesh on 1/10/2017.
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder>
{

    private List<Task> mTasks;
    private Context mContext;

    public TasksAdapter(Context context, List<Task> tasks)
    {
        this.mTasks = tasks;
        this.mContext = context;
    }

    private Context getContext()
    {
        return this.mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View taskView = inflater.inflate(R.layout.task_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(taskView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Task task = this.mTasks.get(position);

        TextView textView = holder.nameTextView;
        textView.setText(task.getDescription().toString());

        Button button = holder.messageButton;
        button.setText("P");
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView nameTextView;
        public Button messageButton;
        public CheckBox doneCheckBox;


        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.item_text_view);
            messageButton = (Button) itemView.findViewById(R.id.item_progress);
            doneCheckBox = (CheckBox) itemView.findViewById(R.id.item_check_box);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                Task task = mTasks.get(position);

                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra(MainActivity.PARENT_UUID, task.getTaskId());
                mContext.startActivity(intent);
            }
        }
    }
}
