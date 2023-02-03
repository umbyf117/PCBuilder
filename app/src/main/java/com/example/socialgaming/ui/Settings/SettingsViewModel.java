package com.example.socialgaming.ui.Settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {
    private final MutableLiveData<String> Text;

    public SettingsViewModel(){
        Text = new MutableLiveData<>();
        Text.setValue("Settings Fragment");
    }

    public LiveData<String> getText(){
        return Text;
    }
}
