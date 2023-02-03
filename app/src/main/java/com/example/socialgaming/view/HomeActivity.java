package com.example.socialgaming.view;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialgaming.R;
import com.example.socialgaming.ui.Build.BuildFragment;
import com.example.socialgaming.ui.Search.SearchFragment;
import com.example.socialgaming.ui.Settings.SettingsFragment;
import com.example.socialgaming.ui.home.HomeFragment;
import com.example.socialgaming.ui.profile.ProfileFragment;
import com.example.socialgaming.view.model.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseUser;


public class HomeActivity extends AppCompatActivity {

    private HomeViewModel viewModel;
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    BuildFragment buildFragment = new BuildFragment();
    SearchFragment searchFragment = new SearchFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.bottom_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;
                    case R.id.bottom_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                        return true;
                    case R.id.bottom_build:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, buildFragment).commit();
                        return true;
                    case R.id.bottom_search:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, searchFragment).commit();
                        return true;
                    case R.id.bottom_settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).commit();
                        return true;
                }
                return false;
            }
        });


    }

    public FirebaseUser getUserData() {
        return viewModel.getUserLiveData().getValue();
    }

}
