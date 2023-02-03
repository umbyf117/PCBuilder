package com.example.socialgaming.ui.home;

import android.app.Application;
import android.widget.LinearLayout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.socialgaming.data.Build;
import com.example.socialgaming.repository.component.BuildRepository;
import com.example.socialgaming.repository.user.AuthRepository;
import com.example.socialgaming.repository.user.UserRepository;

import org.checkerframework.checker.units.qual.A;

public class HomeFragmentViewModel extends ViewModel {

    private static final int BUILD_PER_LOAD = 10;

    private BuildRepository buildRepository;
    private AuthRepository authRepository;
    private UserRepository userRepository;

    public HomeFragmentViewModel(Application application) {
        buildRepository = new BuildRepository();
        authRepository = new AuthRepository(application);
        userRepository = new UserRepository();
    }

    //GETTER
    public BuildRepository getBuildRepository() {
        return buildRepository;
    }
    public AuthRepository getAuthRepository() {
        return authRepository;
    }
    public UserRepository getUserRepository() {
        return userRepository;
    }
}