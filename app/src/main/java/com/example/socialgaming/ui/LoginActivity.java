package com.example.socialgaming.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.socialgaming.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class LoginActivity extends Activity {

    private TextView logBut;
    private TextView regBut;
    private EditText em; //email
    private EditText pw; //password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        em = findViewById(R.id.txtMail);
        em.setAutofillHints(View.AUTOFILL_HINT_EMAIL_ADDRESS);
        pw = findViewById(R.id.txtPassword);
        pw.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        GoogleSignInClient gsic = GoogleSignIn.getClient(this, gso);




        logBut = (TextView) findViewById(R.id.logButton);
        logBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkData()){
                    Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(i);
                }
            }
        });

        regBut = (TextView) findViewById(R.id.regButton);
        regBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

    }

    boolean isEmail(EditText text){
        CharSequence email = text.getText().toString();
        return(!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isPassword(EditText text){
        CharSequence pw = text.getText().toString();
        return(!TextUtils.isEmpty(pw) && !(pw.length()<8));
    }

    boolean isEmpty(EditText text){
        CharSequence cs = text.getText().toString();
        return TextUtils.isEmpty(cs);
    }

    boolean checkData(){
        if(isEmpty(em) || !(isEmail(em))){
            em.setError("Email address not valid");
            return false;
        }

        if(isEmpty(pw) || pw.length()<8 || !(isPassword(pw))){
            pw.setError("Password not valid");
            return false;
        }
        return true;
    }
}