package com.example.enrgydrinksgpts.storage;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

public class DataManager {
    private static final String PREFS_NAME = "my_app_prefs";
    private static final String KEY_DATA = "key_data";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public DataManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveData(MyData data) {
        String jsonData = gson.toJson(data);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_DATA, jsonData);
        editor.apply();
    }

    public MyData loadData() {
        String jsonData = sharedPreferences.getString(KEY_DATA, null);
        if (jsonData != null) {
            return gson.fromJson(jsonData, MyData.class);
        }
        return null;
    }
}