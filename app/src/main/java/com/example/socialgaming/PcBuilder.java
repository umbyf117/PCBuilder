package com.example.socialgaming;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.socialgaming.view.MainActivity;
import com.example.socialgaming.utils.ViewUtils;

public class PcBuilder extends Application {

    private static final String PREFS_NAME = "NightModePrefs";
    private static final String KEY_IS_NIGHT_MODE = "isNightMode";

    private MainActivity mainActivity;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void showError(String msg) {
        ViewUtils.displaySnackbar(mainActivity.findViewById(R.id.main_activity), msg);
    }

    public void setMainActivity(MainActivity activity) {
        this.mainActivity = activity;
    }
    public void backPress() {
        mainActivity.finish();
    }

    public static void setNightMode(Context context, boolean isNightMode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_NIGHT_MODE, isNightMode);
        editor.apply();
    }

    // Ottieni lo stato del modo notte/modo chiaro dalle preferenze condivise
    public static boolean getNightMode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_NIGHT_MODE, false);
    }
}
