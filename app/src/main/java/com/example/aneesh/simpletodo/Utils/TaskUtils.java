package com.example.aneesh.simpletodo.Utils;

import com.example.aneesh.simpletodo.model.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.os.ParcelFileDescriptor.MODE_WORLD_WRITEABLE;

/**
 * Created by Aneesh on 1/8/2017.
 */

/**
 * Created by Maya on 1/14/2017.
 */
public class TaskUtils {

    public static List<Task> taskList;

    public static void setTaskList(List<Task> tasks)
    {
        taskList = tasks;
    }

    public static List<Task> generateTasks()
    {
        if (taskList != null)
        {
            return taskList;
        }

        taskList = new ArrayList<>(20);

        //parent tasks
        Task task_1 = new Task("Parent Task 1");
        Task task_2 = new Task("Parent Task 2");
        Task task_3 = new Task("Parent Task 3");

        //set isDone
        task_2.setDone(true);

        taskList.add(task_1);
        taskList.add(task_2);
        taskList.add(task_3);

        //First children
        Task task_2_1 = new Task("Sub Task 2_1", task_2.getTaskId());
        Task task_2_2 = new Task("Sub Task 2_2", task_2.getTaskId());
        Task task_2_3 = new Task("Sub Task 2_3", task_2.getTaskId());

        Task task_3_1 = new Task("Sub Task 3_1", task_3.getTaskId());
        Task task_3_2 = new Task("Sub Task 3_2", task_3.getTaskId());

        task_2_2.setDone(true);

        taskList.add(task_2_1);
        taskList.add(task_2_2);
        taskList.add(task_2_3);
        taskList.add(task_3_1);
        taskList.add(task_3_2);

        //Second children
        Task task_2_1_1 = new Task("Sub Task 2_1_1", task_2_1.getTaskId());
        Task task_2_1_2 = new Task("Sub Task 2_1_2", task_2_1.getTaskId());

        Task task_3_2_1 = new Task("Sub Task 3_2_1", task_3_2.getTaskId());
        Task task_3_2_2 = new Task("Sub Task 3_2_2", task_3_2.getTaskId());

        taskList.add(task_2_1_1);
        taskList.add(task_2_1_2);
        taskList.add(task_3_2_1);
        taskList.add(task_3_2_1);

        return taskList;
    }

    public static Task parseJSON(String json) {
        Gson gson = new GsonBuilder().create();
        Task task = gson.fromJson(json, Task.class);
        return task;
    }

    public static String convertToJSON(List<Task> tasks)
    {
        Gson gson = new Gson();
        String json = gson.toJson(tasks);
        return json;
    }

    public static void writeJSONToFile(String json, FileOutputStream fos) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
        writer.write(json);
        writer.close();
    }

    public static String readJSONFromFile(BufferedReader input) throws IOException {
        String json;
        StringBuffer buffer = new StringBuffer();
        while ((json = input.readLine()) != null) {
            buffer.append(json + "\n");
        }
        String text = buffer.toString();
        return text;
    }

    public static List<Task> convertFromJSON(String json)
    {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Task>>(){}.getType();
        List<Task> tasks = (List<Task>) gson.fromJson(json, listType);
        return tasks;
    }

    public static List<Task> getParentTasks(List<Task> inputList)
    {
        List<Task> parentTasks = new ArrayList<>(10);

        for (Task task: inputList)
        {
            if (task.getParentTaskId() == null)
            {
                parentTasks.add(task);
            }
        }

        return parentTasks;
    }

    public static List<Task> getChildTasks(List<Task> inputList, UUID parentTask)
    {
        List<Task> childTasks = new ArrayList<>(10);

        for (Task task: inputList)
        {
            if (task.getParentTaskId() != null && task.getParentTaskId().equals(parentTask))
            {
                childTasks.add(task);
            }
        }
        return childTasks;
    }

    public static Task getTaskForUUID (UUID taskID)
    {
        Task candidateTask = null;

        for (Task task : taskList)
        {
            if (taskID != null && task.getTaskId() != null && taskID.equals(task.getTaskId()))
            {
                candidateTask = task;
                break;
            }
        }

        return candidateTask;
    }
}