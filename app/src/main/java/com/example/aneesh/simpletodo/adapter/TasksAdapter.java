package com.example.aneesh.simpletodo.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.aneesh.simpletodo.MainActivity;
import com.example.aneesh.simpletodo.R;
import com.example.aneesh.simpletodo.Utils.TaskUtils;
import com.example.aneesh.simpletodo.activity.EditItemActivity;
import com.example.aneesh.simpletodo.fragment.MoveFragment;
import com.example.aneesh.simpletodo.model.SortSetting;
import com.example.aneesh.simpletodo.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static com.example.aneesh.simpletodo.MainActivity.EDIT_ACTIVITY_CODE;
import static com.example.aneesh.simpletodo.Utils.TaskShareFormatter.getFormattedTask;

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

        final TextView textView = holder.mItemTextView;
        Button progressButton = holder.mItemButton;
        CheckBox checkBox = holder.mItemCheckbox;

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    if (isChecked)
                                                    {
                                                        task.setDone(true);
                                                        boolean allSubTaskDone = checkAllSubTasksDone();
                                                        textView.setTextColor(Color.GRAY);

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
                                                        textView.setTextColor(Color.BLACK);
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

        private void shareIntent(List<Task> tasks) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, getFormattedTask(tasks));
            try {
                context.startActivity(shareIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, "No apps have not been installed to share", Toast.LENGTH_SHORT).show();
            }
        }

        private void showLongPressedDialog(final Task task) {
            new MaterialDialog.Builder(context)
                    .title(task.getDescription())
                    .items(R.array.long_click_items)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                            switch (which)
                            {
                                case 0:
                                    ClipboardManager clipboard = (ClipboardManager)
                                            context.getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("simple text",task.getDescription());
                                    clipboard.setPrimaryClip(clip);
                                    Toast.makeText(context, "Copied text to clipboard", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    List<Task> sharedTasks = TaskUtils.getChildTasks(TaskUtils.generateTasks(), task.getTaskId());
                                    sharedTasks.add(task);
                                    shareIntent(sharedTasks);
                                    break;
                                case 2:
                                    showMoveFragment(task.getParentTaskId(), task);
                                    break;
                                case 3:
                                    Intent intent = new Intent(context, EditItemActivity.class);
                                    intent.putExtra(MainActivity.TASK_DESCRIPTION, task.getDescription());
                                    intent.putExtra(MainActivity.EDIT_TASK_UUID, task.getTaskId());
                                    ((Activity)context).startActivityForResult(intent, EDIT_ACTIVITY_CODE);
                                    break;
                                case 4:
                                    List<Task> toDeleteItems = TaskUtils.getChildTasks(TaskUtils.generateTasks(), task.getTaskId());
                                    toDeleteItems.add(task);
                                    deleteItems(toDeleteItems, task);
                                    break;

                                default:
                                        break;
                            }

                            Toast.makeText(context, "Showing "+which, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }

        private void showLongPressedDialogForList(final Task task) {
            new MaterialDialog.Builder(context)
                    .title(task.getDescription())
                    .items(R.array.long_click_items_list)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                            switch (which)
                            {
                                case 0:
                                    ClipboardManager clipboard = (ClipboardManager)
                                            context.getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("simple text",task.getDescription());
                                    clipboard.setPrimaryClip(clip);
                                    Toast.makeText(context, "Copied text to clipboard", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    ClipboardManager formatClipboard = (ClipboardManager)
                                            context.getSystemService(Context.CLIPBOARD_SERVICE);
                                    List<Task> tasks = new ArrayList<Task>();
                                    tasks.add(task);
                                    ClipData formatClip = ClipData.newPlainText("simple text",getFormattedTask(tasks));
                                    formatClipboard.setPrimaryClip(formatClip);
                                    Toast.makeText(context, "Copied text to clipboard", Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    List<Task> sharedTasks = TaskUtils.getChildTasks(TaskUtils.generateTasks(), task.getTaskId());
                                    sharedTasks.add(task);
                                    shareIntent(sharedTasks);
                                    break;
                                case 3:
                                    showMoveFragment(task.getParentTaskId(), task);
                                    break;
                                case 4:
                                    Intent intent = new Intent(context, EditItemActivity.class);
                                    intent.putExtra(MainActivity.TASK_DESCRIPTION, task.getDescription());
                                    intent.putExtra(MainActivity.EDIT_TASK_UUID, task.getTaskId());
                                    ((Activity)context).startActivityForResult(intent, EDIT_ACTIVITY_CODE);
                                    break;
                                case 5:
                                    List<Task> toDeleteItems = TaskUtils.getChildTasks(TaskUtils.generateTasks(), task.getTaskId());
                                    toDeleteItems.add(task);
                                    deleteItems(toDeleteItems, task);
                                    break;

                                default:
                                    break;
                            }

                            Toast.makeText(context, "Showing "+which, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }


        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) {
                Task task = taskList.get(position);
                if (TaskUtils.getChildTasks(TaskUtils.generateTasks(), task.getTaskId()).size() == 0)
                {
                    showLongPressedDialog(task);
                }
                else
                {
                    showLongPressedDialogForList(task);
                }

            }

            return true;
        }
    }

    private void deleteItems(final List<Task> toDeleteItems, Task task) {
        if (toDeleteItems.size() == 1)
        {
            new MaterialDialog.Builder(context)
                    .title(R.string.delete_dialog_title)
                    .content("Are you sure you want to delete "+ task.getDescription()+" checked items?")
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            deleteFromList(toDeleteItems);
                        }
                    })
                    .show();
        }
        else
        {
            new MaterialDialog.Builder(context)
                    .title(R.string.delete_dialog_title)
                    .content("Are you sure you want to delete "+ task.getDescription()+" checked items and " + (toDeleteItems.size() - 1)+" sub-items?")
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            deleteFromList(toDeleteItems);
                        }
                    })
                    .show();
        }

    }

    private void showMoveFragment(UUID parentUUID, Task taskToMove) {

        List<UUID> taskUUIDs = new ArrayList<>();
        taskUUIDs.add(taskToMove.getTaskId());
        FragmentManager fm = ((MainActivity)context).getSupportFragmentManager();
        MoveFragment moveFragment = MoveFragment.newInstance(parentUUID, taskUUIDs);
        moveFragment.show(fm, "fragment_edit_name");

    }

    private void deleteFromList(List<Task> toDeleteItems) {

        List<Task> newTaskList = TaskUtils.generateTasks();

        Iterator<Task> iterator = newTaskList.iterator();
        while (iterator.hasNext())
        {
            Task task = iterator.next();
            if (toDeleteItems.contains(task))
            {
                iterator.remove();
            }
        }

        notifyDataSetChanged();

        ((MainActivity)context).swapAdapterData(newTaskList, true);

        Toast.makeText(context , "Deleted "+ toDeleteItems.size() + " items " , Toast.LENGTH_SHORT).show();

    }
}