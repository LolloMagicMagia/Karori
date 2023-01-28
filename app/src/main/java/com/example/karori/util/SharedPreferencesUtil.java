package com.example.karori.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private final Application application;

    public SharedPreferencesUtil(Application application) { this.application = application; }

    public void writeStringData(String sharedPreferencesFileName, String key, String value){
        SharedPreferences sharedPref = application.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void writeIntData(String sharedPreferencesFileName, String key, int value){
        SharedPreferences sharedPref = application.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public String readStringData(String sharedPreferencesFileName, String key) {
        SharedPreferences sharedPref = application.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }

    public int readIntData(String sharedPreferencesFileName, String key) {
        SharedPreferences sharedPref = application.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE);

        return sharedPref.getInt(key, 0);
    }

    public void deleteAll(String sharedPreferencesFileName) {
        SharedPreferences sharedPref = application.getSharedPreferences(sharedPreferencesFileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}
