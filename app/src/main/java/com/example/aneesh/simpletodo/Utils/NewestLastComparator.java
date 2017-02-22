package com.example.aneesh.simpletodo.Utils;

import com.example.aneesh.simpletodo.model.Task;

import java.util.Comparator;

/**
 * Created by Aneesh on 2/22/2017.
 */

public class NewestLastComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        return o2.getCreatedDate().compareTo(o1.getCreatedDate());
    }
}
