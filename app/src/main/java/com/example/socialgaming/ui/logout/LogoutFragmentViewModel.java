package com.example.socialgaming.ui.logout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogoutFragmentViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LogoutFragmentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("logout");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
