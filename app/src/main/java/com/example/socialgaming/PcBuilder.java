package com.example.socialgaming;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.socialgaming.view.MainActivity;
import com.example.socialgaming.utils.ViewUtils;

public class PcBuilder extends Application {

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
}
