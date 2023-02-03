package com.example.socialgaming.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.example.socialgaming.R;
import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.User;
import com.example.socialgaming.ui.Build.BuildFragment;
import com.example.socialgaming.ui.Search.SearchFragment;
import com.example.socialgaming.ui.Settings.SettingsFragment;
import com.example.socialgaming.ui.home.HomeFragment;
import com.example.socialgaming.ui.profile.ProfileFragment;
import com.example.socialgaming.utils.FragmentUtils;
import com.example.socialgaming.view.auth.LoginFragment;
import com.example.socialgaming.view.model.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private BuildFragment buildFragment;
    private SearchFragment searchFragment;
    private SettingsFragment settingsFragment;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new MainViewModel(getApplication());

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        buildFragment = new BuildFragment();
        searchFragment = new SearchFragment();
        settingsFragment = new SettingsFragment();

        setContentView(R.layout.activity_main);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container_home, homeFragment);
        fragmentTransaction.commit();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setupNavigationListener();

        viewModel.getUserLiveData().observe(this, firebaseUser -> {
            if(firebaseUser == null || viewModel.getUserRepository().getUserData(firebaseUser.getDisplayName()) == null)
                FragmentUtils.startActivity(this, new Intent(MainActivity.this, LoginActivity.class), true);
            else if (firebaseUser != null) {
                user = viewModel.getUserRepository().getUserData(firebaseUser.getDisplayName());
            }
        });

    }

    public void setupNavigationListener() {

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.bottom_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_home, homeFragment).commit();
                    return true;
                case R.id.bottom_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_home, profileFragment).commit();
                    return true;
                case R.id.bottom_build:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_home, buildFragment).commit();
                    return true;
                case R.id.bottom_search:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_home, searchFragment).commit();
                    return true;
                case R.id.bottom_settings:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_home, settingsFragment).commit();
                    return true;
            }
            return false;
        });

    }

    public FirebaseUser getUserData() {
        return viewModel.getUserLiveData().getValue();
    }

}
