package com.example.socialgaming.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.socialgaming.repository.user.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class HomeViewModel extends AndroidViewModel {

    private AuthRepository authRepository;
    private MutableLiveData<FirebaseUser> userLiveData;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        authRepository =AuthRepository.getInstance(getApplication());
        userLiveData = authRepository.getUserLiveData();
    }

    public void logout() {
        authRepository.logOut();
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
}
