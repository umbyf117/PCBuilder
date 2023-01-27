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

    private Button signInButton;
    private TextView signUpText;
    private TextView forgetPasswordText;
    private TextInputLayout emailInputLogin;
    private EditText emailTextLogin;
    private TextInputLayout passwordInputLogin;
    private EditText passwordTextLogin;

    private Button signUpButton;
    private TextView signInText;
    private TextInputLayout emailInputRegister;
    private EditText emailTextRegister;
    private TextInputLayout passwordInputRegister;
    private EditText passwordTextRegister;
    private TextInputLayout usernameInputRegister;
    private EditText usernameTextRegister;

    private View.OnClickListener switchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.fragment_login);

        //Open Homepage once logged
        loginViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if(firebaseUser != null)
                FragmentUtils.startActivity(this, new Intent(LoginActivity.this, HomeActivity.class), true);
        });

        loginViewModel.getErrorLiveData().observe(this, s -> {
            if(s != null && !TextUtils.isEmpty(s))
                ViewUtils.displaySnackbar(findViewById(R.id.login_activity), s);
        });

        inizializeUI();
        listernersSetup();

    }

    private void inizializeUI() {

        login = new LoginFragment();
        register = new RegisterFragment();

        signInButton = findViewById(R.id.loginButton);
        signUpText = findViewById(R.id.signup_text);
        forgetPasswordText = findViewById(R.id.forgetPassword);
        emailInputLogin = findViewById(R.id.textInputMailLogin);
        emailTextLogin = findViewById(R.id.txtMailLogin);
        passwordInputLogin = findViewById(R.id.textInputPasswordLogin);
        passwordTextLogin = findViewById(R.id.txtPasswordLogin);
        signUpButton = findViewById(R.id.registerButton);
        signInText = findViewById(R.id.signin_text);
        emailInputRegister = findViewById(R.id.textInputMailReg);
        emailTextRegister = findViewById(R.id.txtMailRegister);
        passwordInputRegister = findViewById(R.id.textInputPassword);
        passwordTextRegister = findViewById(R.id.txtPasswordRegister);
        usernameInputRegister = findViewById(R.id.textInputUsername);
        usernameTextRegister = findViewById(R.id.txtUsername);

    }

    private void listernersSetup(){

        switchListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchMode();
            }
        };

        //SIGN IN BUTTON
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = emailTextRegister.getText().toString().trim();
                String pass = passwordTextRegister.getText().toString();
                String username = usernameTextRegister.getText().toString().trim();
                if(checkCredentials(mail, pass))
                    loginViewModel.login(mail, pass);

            }
        });

        //SIGN UP BUTTON
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = emailTextLogin.getText().toString().trim();
                String pass = passwordTextLogin.getText().toString();
                if(checkCredentials(mail, pass))
                    loginViewModel.login(mail, pass);

            }
        });

        //SIGN UP & IN SWITCH
        signUpText.setOnClickListener(switchListener);
        signInText.setOnClickListener(switchListener);



    }

    private boolean checkCredentials(String email, String password) {
        boolean check = true;
        if(!StringUtils.checkEmail(email)) {
            check = false;
            ViewUtils.displayErrorStatus(emailInputLogin, getString(R.string.authentication_wrong_format_email));
        }
        if(!StringUtils.checkPassword(password)) {
            check = false;
            ViewUtils.displayErrorStatus(passwordInputLogin, getString(R.string.authentication_wrong_format_password));
        }
        return check;
    }

    private boolean checkCredentials(String email, String password, String username) {
        boolean check = true;
        if(!StringUtils.checkUsername(username)){
            check = false;
            ViewUtils.displayErrorStatus(usernameInputRegister, getString(R.string.authentication_input_wrong_format_username));
        }
        if(!StringUtils.checkEmail(email)) {
            check = false;
            ViewUtils.displayErrorStatus(emailInputLogin, getString(R.string.authentication_wrong_format_email));
        }
        if(!StringUtils.checkPassword(password)) {
            check = false;
            ViewUtils.displayErrorStatus(passwordInputLogin, getString(R.string.authentication_wrong_format_password));
        }
        return check;
    }

    public void switchMode() {

        String email = switchControls();

        if(loginScreen) {
            loginScreen = false;
            setContentView(login.getView());
            emailTextRegister.setText(email);
        }
        else {
            loginScreen = true;
            setContentView(register.getView());
            emailTextLogin.setText(email);
        }


    }

    private void clearControls() {
        emailTextLogin.getText().clear();
        passwordTextLogin.getText().clear();
        emailTextRegister.getText().clear();
        passwordTextRegister.getText().clear();
        usernameTextRegister.getText().clear();

    }

    private String switchControls() {

        String email;
        if(loginScreen)
            email = emailTextLogin.getText().toString();
        else
            email = emailTextRegister.getText().toString();

        clearControls();
        return email;
    }

}