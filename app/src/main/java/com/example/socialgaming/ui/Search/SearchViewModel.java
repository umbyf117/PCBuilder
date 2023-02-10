package com.example.socialgaming.ui.Search;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.socialgaming.repository.component.BuildRepository;
import com.example.socialgaming.repository.user.AuthRepository;
import com.example.socialgaming.repository.user.UserRepository;

public class SearchViewModel extends ViewModel {

    private BuildRepository buildRepository;
    private AuthRepository authRepository;
    private UserRepository userRepository;

    public SearchViewModel(Application application) {
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
