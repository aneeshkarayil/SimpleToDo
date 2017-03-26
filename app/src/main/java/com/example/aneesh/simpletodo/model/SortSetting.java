package com.example.aneesh.simpletodo.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by Aneesh on 3/19/2017.
 */

public class SortSetting {

    private String sortSetting;

    private static SortSetting sortSettingInstance;

    public static SortSetting getInstance()
    {
        if (sortSettingInstance == null)
        {
            sortSettingInstance = new SortSetting("Manual");
        }
        return sortSettingInstance;
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
        Type type = new TypeToken<String>(){}.getType();
        String operation = (String) gson.fromJson(json, type);
        getInstance().setSortSetting(operation);
        return getInstance();
    }
}
