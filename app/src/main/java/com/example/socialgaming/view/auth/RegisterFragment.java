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
import androidx.lifecycle.ViewModelProvider;

import com.example.socialgaming.R;
import com.example.socialgaming.utils.StringUtils;
import com.example.socialgaming.utils.ViewUtils;
import com.example.socialgaming.view.LoginActivity;
import com.example.socialgaming.view.model.LoginViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends Fragment {

    private LoginViewModel viewModel;
    private Button signUpButton;
    private TextView signInText;
    private TextInputLayout emailInputRegister;
    private EditText emailTextRegister;
    private TextInputLayout passwordInputRegister;
    private EditText passwordTextRegister;
    private TextInputLayout usernameInputRegister;
    private EditText usernameTextRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new LoginViewModel(getActivity().getApplication());
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inizializeUI(view);
        listernersSetup();
    }

    private void inizializeUI(View view) {

        signUpButton = view.findViewById(R.id.registerButton);
        signInText = view.findViewById(R.id.signin_text);
        emailInputRegister = view.findViewById(R.id.textInputMailReg);
        emailTextRegister = view.findViewById(R.id.txtMailRegister);
        passwordInputRegister = view.findViewById(R.id.textInputPassword);
        passwordTextRegister = view.findViewById(R.id.txtPasswordRegister);
        usernameInputRegister = view.findViewById(R.id.textInputUsername);
        usernameTextRegister = view.findViewById(R.id.txtUsername);

    }

    private void listernersSetup(){

        //SIGN UP BUTTON
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = emailTextRegister.getText().toString().trim();
                String pass = passwordTextRegister.getText().toString();
                String username = usernameTextRegister.getText().toString().trim();
                if(checkCredentials(mail, pass, username))
                    viewModel.register(mail, pass, username);

            }
        });

        signInText.setOnClickListener(LoginActivity.getSwitchListener());

    }

    public boolean checkCredentials(String email, String password, String username) {
        boolean check = true;
        if(!StringUtils.checkUsername(username)){
            check = false;
            ViewUtils.displayErrorStatus(usernameInputRegister, getString(R.string.authentication_input_wrong_format_username));
        }
        if(!StringUtils.checkEmail(email)) {
            check = false;
            ViewUtils.displayErrorStatus(emailInputRegister, getString(R.string.authentication_wrong_format_email));
        }
        if(!StringUtils.checkPassword(password)) {
            check = false;
            ViewUtils.displayErrorStatus(passwordInputRegister, getString(R.string.authentication_wrong_format_password));
        }
        return check;
    }

    //GETTER & SETTER
    public Button getSignUpButton() {
        return signUpButton;
    }

    public void setSignUpButton(Button signUpButton) {
        this.signUpButton = signUpButton;
    }

    public TextView getSignInText() {
        return signInText;
    }

    public void setSignInText(TextView signInText) {
        this.signInText = signInText;
    }

    public TextInputLayout getEmailInputRegister() {
        return emailInputRegister;
    }

    public void setEmailInputRegister(TextInputLayout emailInputRegister) {
        this.emailInputRegister = emailInputRegister;
    }

    public EditText getEmailTextRegister() {
        return emailTextRegister;
    }

    public void setEmailTextRegister(EditText emailTextRegister) {
        this.emailTextRegister = emailTextRegister;
    }

    public TextInputLayout getPasswordInputRegister() {
        return passwordInputRegister;
    }

    public void setPasswordInputRegister(TextInputLayout passwordInputRegister) {
        this.passwordInputRegister = passwordInputRegister;
    }

    public EditText getPasswordTextRegister() {
        return passwordTextRegister;
    }

    public void setPasswordTextRegister(EditText passwordTextRegister) {
        this.passwordTextRegister = passwordTextRegister;
    }

    public TextInputLayout getUsernameInputRegister() {
        return usernameInputRegister;
    }

    public void setUsernameInputRegister(TextInputLayout usernameInputRegister) {
        this.usernameInputRegister = usernameInputRegister;
    }

    public EditText getUsernameTextRegister() {
        return usernameTextRegister;
    }

    public void setUsernameTextRegister(EditText usernameTextRegister) {
        this.usernameTextRegister = usernameTextRegister;
    }
}
