package com.example.aneesh.simpletodo.model;

import com.example.aneesh.simpletodo.Utils.NewestFirstComparator;
import com.example.aneesh.simpletodo.comparators.AlphabeticalComparator;
import com.example.aneesh.simpletodo.comparators.NewestLastComparator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Comparator;

/**
 * Created by Aneesh on 3/19/2017.
 */

public class SortSetting {

    private String sortSetting;
    private boolean isListsFirst;

    private static SortSetting sortSettingInstance;

    public static SortSetting getInstance()
    {
        if (sortSettingInstance == null)
        {
            sortSettingInstance = new SortSetting("Manual");
        }
        return sortSettingInstance;
    }

    public Comparator<Task> getComparator()
    {
        Comparator comparator = null;

        switch (sortSetting)
        {
            case "Alphabetical":
                comparator = new AlphabeticalComparator();
                break;
            case "Manual":
            case "Newest First":
                comparator = new NewestFirstComparator();
                break;
            case "Newest Last":
                comparator = new NewestLastComparator();
                break;
            default:
                comparator = new NewestFirstComparator();
                break;

        }

        return comparator;
    }

    private SortSetting(String sortSetting)
    {
        this.sortSetting = sortSetting;
    }

    public String getSortSetting() {
        return sortSetting;
    }

    public void setSortSetting(String sortSetting) {
        this.sortSetting = sortSetting;
    }

    public static SortSetting convertFromJSON(String json)
    {
        Gson gson = new Gson();
        String sortSetting = new Gson().fromJson(json, JsonObject.class).get("sortSetting").getAsString();
        boolean listsFirst = new Gson().fromJson(json, JsonObject.class).get("isListsFirst").getAsBoolean();
        getInstance().setSortSetting(sortSetting);
        getInstance().setListsFirst(listsFirst);
        return getInstance();
    }

    public boolean isListsFirst() {
        return isListsFirst;
    }

    public void setListsFirst(boolean listsFirst) {
        isListsFirst = listsFirst;
    }
}
