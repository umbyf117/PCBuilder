package com.example.socialgaming.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.socialgaming.repository.component.BuildRepository;
import com.example.socialgaming.repository.user.AuthRepository;
import com.example.socialgaming.repository.user.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class MainViewModel extends AndroidViewModel {

    private AuthRepository authRepository;
    private UserRepository userRepository;
    private BuildRepository buildRepository;
    private MutableLiveData<FirebaseUser> userLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        authRepository = AuthRepository.getInstance(getApplication());
        userRepository = new UserRepository();
        buildRepository = new BuildRepository();
        userLiveData = authRepository.getUserLiveData();
    }



    public void logout() {
        authRepository.logOut();
    }

    //GETTER
    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
    public AuthRepository getAuthRepository() {
        return authRepository;
    }
    public UserRepository getUserRepository() {
        return userRepository;
    }
    public BuildRepository getBuildRepository() {
        return buildRepository;
    }
}
