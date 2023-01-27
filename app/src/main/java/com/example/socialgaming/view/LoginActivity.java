package com.example.socialgaming.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialgaming.R;
import com.example.socialgaming.utils.*;
import com.example.socialgaming.view.auth.*;
import com.example.socialgaming.view.model.LoginViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private FragmentManager fragmentManager;
    private boolean loginScreen;

    private LoginFragment login;
    private RegisterFragment register;

    private View.OnClickListener switchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_login);

        //Open Homepage once logged
        loginViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if(firebaseUser != null)
                FragmentUtils.startActivity(this, new Intent(LoginActivity.this, HomeActivity.class), true);
        });

        loginViewModel.getErrorLiveData().observe(this, s -> {
            if(s != null && !TextUtils.isEmpty(s))
                ViewUtils.displaySnackbar(findViewById(R.id.login_activity), s);
        });

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.login_activity, login);
        fragmentTransaction.commit();

        inizializeUI();
        listernersSetup();

    }

    private void inizializeUI() {

        login = new LoginFragment(loginViewModel);
        register = new RegisterFragment(loginViewModel);

    }

    private void listernersSetup(){

        switchListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchMode();
            }
        };

        //SIGN UP & IN SWITCH
        login.getSignUpText().setOnClickListener(switchListener);
        register.getSignInText().setOnClickListener(switchListener);

    }

    public void switchMode() {

        String email = switchControls();

        if(loginScreen) {
            loginScreen = false;
            register.getEmailTextRegister().setText(email);
            FragmentUtils.loadFragment(register, fragmentManager, R.id.register_screen);
        }
        else {
            loginScreen = true;
            login.getEmailTextLogin().setText(email);
            FragmentUtils.loadFragment(login, fragmentManager, R.id.login_screen);
        }


    }

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
    }

}