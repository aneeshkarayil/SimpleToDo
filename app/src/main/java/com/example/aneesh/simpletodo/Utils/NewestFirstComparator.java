package com.example.aneesh.simpletodo.Utils;

import com.example.aneesh.simpletodo.model.Task;

import java.util.Comparator;

/**
 * Created by Aneesh on 2/22/2017.
 */

public class NewestFirstComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        return o1.getCreatedDate().compareTo(o2.getCreatedDate());
    }
}
