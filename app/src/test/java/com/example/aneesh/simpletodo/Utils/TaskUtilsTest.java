package com.example.aneesh.simpletodo.Utils;

import com.example.aneesh.simpletodo.model.Task;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Aneesh on 1/8/2017.
 */
public class TaskUtilsTest {

    @Test
    public void createTestTasks() throws Exception {

        List<Task> taskList = TaskUtils.generateTasks();


        Assert.assertEquals("Expected the size to be ", 11, taskList.size());
    }

    @Test
    public void getParentTasks() throws Exception {

        List<Task> taskList = TaskUtils.generateTasks();

        List<Task> parentsTaskList = TaskUtils.getParentTasks(taskList);

        Assert.assertEquals("Expected the size to be ", 3, parentsTaskList.size());
    }

    @Test
    public void getChildTasks() throws Exception {

        List<Task> taskList = TaskUtils.generateTasks();

        Task t1 = null;

        for (Task inputTask: taskList)
        {
            if (inputTask.getDescription().equals("Parent task 1"))
            {
                t1 = inputTask;
            }
        }

        Assert.assertEquals("Expected to have no child for parent task 1", 0, TaskUtils.getChildTasks(taskList, t1.getTaskId()).size());
        Assert.assertEquals("Expected to have no child for parent task 1", 0, TaskUtils.getChildTasks(taskList, t1.getTaskId()).size());

        for (Task inputTask: taskList)
        {
            if (inputTask.getDescription().equals("Parent task 2"))
            {
                t1 = inputTask;
            }
        }

        Assert.assertEquals( 3, TaskUtils.getChildTasks(taskList, t1.getTaskId()).size());
        Assert.assertEquals( 3, TaskUtils.getChildTasks(taskList, t1.getTaskId()).size());

        for (Task inputTask: taskList)
        {
            if (inputTask.getDescription().equals("Sub task 3_2"))
            {
                t1 = inputTask;
            }
        }


        Assert.assertEquals( 2, TaskUtils.getChildTasks(taskList, t1.getTaskId()).size());
        Assert.assertEquals( 2, TaskUtils.getChildTasks(taskList, t1.getTaskId()).size());
    }

}