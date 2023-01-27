package com.example.socialgaming.view.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.socialgaming.R;
import com.example.socialgaming.utils.StringUtils;
import com.example.socialgaming.utils.ViewUtils;
import com.example.socialgaming.view.model.LoginViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

    private LoginViewModel viewModel;
    private Button signInButton;
    private TextView signUpText;
    private TextView forgetPasswordText;
    private TextInputLayout emailInputLogin;
    private EditText emailTextLogin;
    private TextInputLayout passwordInputLogin;
    private EditText passwordTextLogin;

    public LoginFragment(LoginViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inizializeUI(view);
        listernersSetup();
    }

    private void inizializeUI(View view) {

        signInButton = view.findViewById(R.id.loginButton);
        signUpText = view.findViewById(R.id.signup_text);
        forgetPasswordText = view.findViewById(R.id.forgetPassword);
        emailInputLogin = view.findViewById(R.id.textInputMailLogin);
        emailTextLogin = view.findViewById(R.id.txtMailLogin);
        passwordInputLogin = view.findViewById(R.id.textInputPasswordLogin);
        passwordTextLogin = view.findViewById(R.id.txtPasswordLogin);

    }

    private void listernersSetup(){

        //SIGN IN BUTTON
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = emailTextLogin.getText().toString().trim();
                String pass = passwordTextLogin.getText().toString();
                if(checkCredentials(mail, pass))
                    viewModel.login(mail, pass);

            }
        });

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

    //GETTER & SETTER
    public Button getSignInButton() {
        return signInButton;
    }

    public void setSignInButton(Button signInButton) {
        this.signInButton = signInButton;
    }

    public TextView getSignUpText() {
        return signUpText;
    }

    public void setSignUpText(TextView signUpText) {
        this.signUpText = signUpText;
    }

    public TextView getForgetPasswordText() {
        return forgetPasswordText;
    }

    public void setForgetPasswordText(TextView forgetPasswordText) {
        this.forgetPasswordText = forgetPasswordText;
    }

    public TextInputLayout getEmailInputLogin() {
        return emailInputLogin;
    }

    public void setEmailInputLogin(TextInputLayout emailInputLogin) {
        this.emailInputLogin = emailInputLogin;
    }

    public EditText getEmailTextLogin() {
        return emailTextLogin;
    }

    public void setEmailTextLogin(EditText emailTextLogin) {
        this.emailTextLogin = emailTextLogin;
    }

    public TextInputLayout getPasswordInputLogin() {
        return passwordInputLogin;
    }

    public void setPasswordInputLogin(TextInputLayout passwordInputLogin) {
        this.passwordInputLogin = passwordInputLogin;
    }

    public EditText getPasswordTextLogin() {
        return passwordTextLogin;
    }

    public void setPasswordTextLogin(EditText passwordTextLogin) {
        this.passwordTextLogin = passwordTextLogin;
    }

}
