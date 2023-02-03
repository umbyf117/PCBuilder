package com.example.socialgaming.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.socialgaming.R;
import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.User;
import com.example.socialgaming.ui.Build.BuildFragment;
import com.example.socialgaming.ui.Search.SearchFragment;
import com.example.socialgaming.ui.Settings.SettingsFragment;
import com.example.socialgaming.ui.home.HomeFragment;
import com.example.socialgaming.ui.profile.ProfileFragment;
import com.example.socialgaming.utils.FragmentUtils;
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
    private FragmentManager fm;
    private Fragment fragment;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new MainViewModel(getApplication());
        viewModel.getUserLiveData().observe(this, firebaseUser -> {
            if(firebaseUser == null)
                FragmentUtils.startActivity(this, new Intent(MainActivity.this, LoginActivity.class), true);
        });

        user = viewModel.getUserRepository().getUserData(viewModel.getUserLiveData().getValue().getDisplayName());

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        buildFragment = new BuildFragment();
        searchFragment = new SearchFragment();
        settingsFragment = new SettingsFragment();

        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        /*
        getSupportFragmentManager().beginTransaction().replace(R.id.container_home, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.bottom_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_home, new HomeFragment()).commit();
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
            }
        });
        */

        fm = getSupportFragmentManager();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.bottom_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.bottom_profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.bottom_build:
                        fragment = new BuildFragment();
                        break;
                    case R.id.bottom_search:
                        fragment = new SearchFragment();
                        break;
                    case R.id.bottom_settings:
                        fragment = new SettingsFragment();
                        break;
                }
                final FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container_home, fragment).commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.homeFrag);

    }

    public FirebaseUser getUserData() {
        return viewModel.getUserLiveData().getValue();
    }

}
