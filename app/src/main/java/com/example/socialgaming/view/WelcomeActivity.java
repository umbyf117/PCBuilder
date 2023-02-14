package com.example.socialgaming.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialgaming.R;
import com.example.socialgaming.utils.FragmentUtils;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        int welcomeDuration = 1000; // time misured in ms
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentUtils.startActivity(new MainActivity(), new Intent(WelcomeActivity.this, LoginActivity.class), true);
            }
        }, welcomeDuration);
    }
}
