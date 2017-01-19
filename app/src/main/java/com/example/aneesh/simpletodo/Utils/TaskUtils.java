package com.example.aneesh.simpletodo.Utils;

import com.example.aneesh.simpletodo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Aneesh on 1/8/2017.
 */

public class TaskUtils {

    public static List<Task> taskList;

    public static List createTestTasks()
    {
        if (taskList != null)
        {
            return taskList;
        }

        taskList = new ArrayList<>(100);

        Task t1 = new Task("Parent task 1");
        Task t2 = new Task("Parent task 2");
        Task t3 = new Task("Parent task 3");

        //Parent tasks
        taskList.add(t1);
        taskList.add(t2);
        taskList.add(t3);

        //Sub-tasks
        Task t2_t1 = new Task("Sub task 2_1", t2.getTaskId());
        Task t2_t2 = new Task("Sub task 2_2", t2.getTaskId());
        Task t2_t3 = new Task("Sub task 2_3", t2.getTaskId());

        Task t3_t1 = new Task("Sub task 3_1", t3.getTaskId());
        Task t3_t2 = new Task("Sub task 3_2", t3.getTaskId());

        taskList.add(t2_t1);
        taskList.add(t2_t2);
        taskList.add(t2_t3);

        taskList.add(t3_t1);
        taskList.add(t3_t2);

        //Sub sub-tasks

        Task t3_t1_t1 = new Task("Sub task 3_1_1", t3_t1.getTaskId());
        Task t3_t2_t1 = new Task("Sub task 3_2_1", t3_t2.getTaskId());
        Task t3_t2_t2 = new Task("Sub task 3_2_2", t3_t2.getTaskId());

        taskList.add(t3_t1_t1);
        taskList.add(t3_t2_t1);
        taskList.add(t3_t2_t2);

        return taskList;

    }

    public static List<Task> getParentTasks(List<Task> inputTasks)
    {
        List<Task> parentTasks = new ArrayList<>(10);

        for (Task inputTask: inputTasks)
        {
            if (inputTask.getParentId() == null)
            {
                parentTasks.add(inputTask);
            }
        }

        return parentTasks;
    }

    public static List<Task> getChildTasks(List<Task> inputTasks, UUID parentTaskUUID)
    {
        List<Task> childTasks = new ArrayList<>(10);

        for (Task inputTask: inputTasks)
        {
            if (inputTask.getParentId() != null && inputTask.getParentId().equals(parentTaskUUID))
            {
                childTasks.add(inputTask);
            }
        }

        return childTasks;
    }


    public static List<Task> getChildTasks(List<Task> inputTasks, Task parentTask)
    {
        return TaskUtils.getChildTasks(inputTasks, parentTask.getTaskId());
    }
}
