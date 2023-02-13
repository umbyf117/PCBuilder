package com.example.socialgaming.ui.Settings;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static SharedPrefManager instance;
    private static Context ctx;

    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String KEY_NIGHT_MODE = "night_mode";

    private SharedPrefManager(Context context){
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if(instance == null){
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void setNighModeState(Boolean state){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NIGHT_MODE, state);
        editor.apply();
    }

    public boolean loadNightModeState(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_NIGHT_MODE, false);
    }

}
