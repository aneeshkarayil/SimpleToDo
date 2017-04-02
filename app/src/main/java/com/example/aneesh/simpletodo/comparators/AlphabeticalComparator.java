package com.example.aneesh.simpletodo.comparators;

import com.example.aneesh.simpletodo.Utils.TaskUtils;
import com.example.aneesh.simpletodo.model.Task;

import java.util.Comparator;

/**
 * Created by Aneesh on 3/29/2017.
 */

public class AlphabeticalComparator implements Comparator<Task> {

    private boolean isListFirst;

    public AlphabeticalComparator(boolean isListFirst)
    {
        this.isListFirst = isListFirst;
    }

    @Override
    public int compare(Task task1, Task task2) {

        if (isListFirst)
        {
            if (TaskUtils.getChildTasks(TaskUtils.generateTasks(), task1.getTaskId()).size() > 0 && TaskUtils.getChildTasks(TaskUtils.generateTasks(), task2.getTaskId()).size() == 0 )
            {
                return -1;
            }
            else if (TaskUtils.getChildTasks(TaskUtils.generateTasks(), task2.getTaskId()).size() > 0 && TaskUtils.getChildTasks(TaskUtils.generateTasks(), task1.getTaskId()).size() == 0 )
            {
                return 1;
            }
        }

        return task1.getDescription().toLowerCase().compareTo(task2.getDescription().toLowerCase());
    }
}
