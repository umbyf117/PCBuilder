package com.example.socialgaming.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> Text;

    public ProfileViewModel(){
        Text = new MutableLiveData<>();
        Text.setValue("Profile Fragment");
    }

    public LiveData<String> getText(){
        return Text;
    }
}
