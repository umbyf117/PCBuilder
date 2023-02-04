package com.example.socialgaming.ui.profile;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.socialgaming.repository.component.BuildRepository;
import com.example.socialgaming.repository.user.AuthRepository;
import com.example.socialgaming.repository.user.UserRepository;

public class ProfileViewModel extends ViewModel {

    private AuthRepository authRepository;
    private UserRepository userRepository;

    public ProfileViewModel(Application application) {
        authRepository = new AuthRepository(application);
        userRepository = new UserRepository();
    }

    //GETTER
    public AuthRepository getAuthRepository() {
        return authRepository;
    }
    public UserRepository getUserRepository() {
        return userRepository;
    }
}
