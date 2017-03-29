package com.example.aneesh.simpletodo.comparators;

import com.example.aneesh.simpletodo.model.Task;

import java.util.Comparator;

/**
 * Created by Aneesh on 3/29/2017.
 */

public class NewestFirstComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        return task1.getCreatedDate().compareTo(task2.getCreatedDate());
    }


}
