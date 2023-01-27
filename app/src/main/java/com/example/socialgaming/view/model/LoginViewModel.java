package com.example.socialgaming.view.model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.socialgaming.repository.callback.AuthenticationCallback;
import com.example.socialgaming.repository.user.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends AndroidViewModel implements AuthenticationCallback {

    private AuthRepository authRepository;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<String> errorLiveData;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        authRepository = AuthRepository.getInstance(getApplication());
        userLiveData = authRepository.getUserLiveData();
        errorLiveData = new MutableLiveData<>();
    }

    public void login(String email, String password) {
        authRepository.login(email, password);
    }

    public void register(String email, String password, String username) {
        authRepository.register(email, password, username);
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getErrorLiveData(){ return errorLiveData; }

    @Override
    public void showAuthError(String msg) {
        errorLiveData.setValue(msg);
    }
}
