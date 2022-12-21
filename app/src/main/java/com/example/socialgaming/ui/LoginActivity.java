package com.example.socialgaming.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialgaming.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {

    private Button logBut;
    private Button regBut;
    private EditText em; //email
    private EditText pw; //password
    private SignInButton gsib;
    private GoogleSignInClient gsic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        em = findViewById(R.id.txtMail);
        em.setAutofillHints(View.AUTOFILL_HINT_EMAIL_ADDRESS);
        pw = findViewById(R.id.txtPassword);
        pw.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        gsic = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });



        logBut = (Button) findViewById(R.id.logButton);
        logBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkData()){
                    Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(i);
                }
            }
        });

        regBut = (Button) findViewById(R.id.regButton);
        regBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

    }

    private void signIn() {
        Intent signInIntent = gsic.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }

    protected void onStart(){
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        }
        catch(ApiException e){
            Log.w("TAG", "signInResult:failed code="+ e.getStatusCode());
            updateUI(null);
        }

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

    public void updateUI(GoogleSignInAccount account){
        if(account != null){
            Toast.makeText(this, "You signed in successfully", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MenuActivity.class));
        }
        else{
            Toast.makeText(this, "You didn't sign in", Toast.LENGTH_LONG).show();
        }
    }
}