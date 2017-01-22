package com.example.aneesh.simpletodo.Utils;

import com.example.aneesh.simpletodo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Aneesh on 1/8/2017.
 */

/**
 * Created by Maya on 1/14/2017.
 */
public class TaskUtils {

    private static List<Task> taskList;

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