package com.example.socialgaming.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialgaming.R;
import com.example.socialgaming.utils.*;
import com.example.socialgaming.view.auth.*;
import com.example.socialgaming.view.model.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private boolean loginScreen;
    private LoginViewModel loginViewModel;

    private static View.OnClickListener switchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        fragmentManager = getSupportFragmentManager();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.login_activity, new LoginFragment());
        fragmentTransaction.commit();

        listernersSetup();

        //Open Homepage once logged
        loginViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if(firebaseUser != null)
                FragmentUtils.startActivity(this, new Intent(LoginActivity.this, MainActivity.class), true);
        });

        loginViewModel.getErrorLiveData().observe(this, s -> {
            if(s != null && !TextUtils.isEmpty(s))
                ViewUtils.displaySnackbar(findViewById(R.id.login_activity), s);
        });

    }

    private void listernersSetup(){

        switchListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchMode();
            }
        };

    }

    public void switchMode() {

        if(loginScreen) {
            loginScreen = false;
            FragmentUtils.loadFragment(new RegisterFragment(), fragmentManager, R.id.login_activity);

        }
        else {
            loginScreen = true;
            FragmentUtils.loadFragment(new LoginFragment(), fragmentManager, R.id.login_activity);
        }

    }

    public static View.OnClickListener getSwitchListener() {
        return switchListener;
    }
    /*
    private void clearControls() {
        login.getEmailTextLogin().getText().clear();
        login.getPasswordTextLogin().getText().clear();
        register.getEmailTextRegister().getText().clear();
        register.getPasswordTextRegister().getText().clear();
        register.getUsernameTextRegister().getText().clear();

    }

    private String switchControls() {

        String email;
        if(loginScreen)
            email = login.getEmailTextLogin().getText().toString();
        else
            email = register.getEmailTextRegister().getText().toString();

        clearControls();
        return email;
    }*/

}