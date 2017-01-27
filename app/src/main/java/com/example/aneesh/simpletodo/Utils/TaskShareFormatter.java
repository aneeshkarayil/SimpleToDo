package com.example.aneesh.simpletodo.Utils;

import com.example.aneesh.simpletodo.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Aneesh on 1/26/2017.
 */

public class TaskShareFormatter {

    public static String getFormattedTask(UUID parentTaskID)
    {
        List<Task> taskList = TaskUtils.generateTasks();
        StringBuilder builder = new StringBuilder();

        Map<UUID,List<Task>> tree  = new HashMap<UUID,List<Task>>();
        List<Task>           roots = new ArrayList<Task>();
        for(Task n : taskList) {
            if(n.getParentTaskId() == null)
                roots.add(n);
            else {
                if(!tree.containsKey(n.getParentTaskId()))
                    tree.put(n.getParentTaskId(), new ArrayList<Task>());
                tree.get(n.getParentTaskId()).add(n);
            }
        }

        tree.put(null, roots);

        for (Task root: roots)
        {
            builder.append("\t"+getTaskHierarchy(root, tree)+"\n");
        }

        return builder.toString();
    }


    public static String getTaskHierarchy(Task task, Map<UUID,List<Task>>  uuidMap)
    {
        String description = task.getDescription()+"\n";

        if (uuidMap.get(task.getTaskId()) == null)
        {
            return description;
        }

        List<Task> tasks = uuidMap.get(task.getTaskId());

        String childDescription = "";

        for (Task childTasks: tasks)
        {
            childDescription = childDescription + "\t -"+childTasks.getDescription()+"\n"+ getTaskHierarchy(childTasks, uuidMap);
        }

        return description + childDescription;
    }

}
