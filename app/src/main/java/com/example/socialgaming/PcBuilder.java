package com.example.socialgaming;

import android.app.Application;

import com.example.socialgaming.view.HomeActivity;
import com.example.socialgaming.utils.ViewUtils;

public class PcBuilder extends Application {

    private HomeActivity homeActivity;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void showError(String msg) {
        ViewUtils.displaySnackbar(homeActivity.findViewById(R.id.home_activity), msg);
    }

    public void setMainActivity(HomeActivity activity) {
        this.homeActivity = activity;
    }

    public void backPress() {
        homeActivity.finish();
    }
}
