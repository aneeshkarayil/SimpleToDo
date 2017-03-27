package com.example.aneesh.simpletodo.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
        String sortSetting = new Gson().fromJson(json, JsonObject.class).get("sortSetting").toString();
        getInstance().setSortSetting(sortSetting);
        return getInstance();
    }
}
