package com.example.socialgaming.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.socialgaming.Interfaces.OnCardSelectedListener;
import com.example.socialgaming.PcBuilder;
import com.example.socialgaming.R;
import com.example.socialgaming.data.User;
import com.example.socialgaming.repository.callbacks.IUserCallback;
import com.example.socialgaming.ui.Build.BuildFragment;
import com.example.socialgaming.ui.Search.SearchFragment;
import com.example.socialgaming.ui.Settings.SettingsFragment;
import com.example.socialgaming.ui.Settings.SharedPrefManager;
import com.example.socialgaming.ui.home.HomeFragment;
import com.example.socialgaming.ui.profile.ProfileFragment;
import com.example.socialgaming.utils.FragmentUtils;
import com.example.socialgaming.view.model.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

@SuppressLint("ResourceType")
public class MainActivity extends AppCompatActivity implements IUserCallback {

    public static final String TAG = "Main Activity";

    public ColorStateList colorDark;
    public ColorStateList color;
    public ColorStateList background;
    public ColorStateList backgroundDark;
    public ColorStateList textColor;
    public ColorStateList gold;

    private MainViewModel viewModel;
    private User user;
    private boolean modifiedUser;

    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private BuildFragment buildFragment;
    private SearchFragment searchFragment;
    private SettingsFragment settingsFragment;


    private Fragment currentFragment;


    //CREARE IL SALVATAGGIO DATI NEL CASO DI CAMBIO DI FRAGMENT

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setNightMode();

        super.onCreate(savedInstanceState);
        getWindow().setWindowAnimations(R.anim.anim);

        User.DEFAULT_IMAGE = BitmapFactory.decodeResource(this.getResources(), R.drawable.logo);
        instantiateColors();

        viewModel = new MainViewModel(getApplication());
        viewModel.getUserLiveData().observe(this, firebaseUser -> {
            if(firebaseUser == null)
                FragmentUtils.startActivity(this, new Intent(MainActivity.this, LoginActivity.class), true);
            });
        //viewModel.getUserRepository().getUserData(viewModel.getUserLiveData().getValue().getDisplayName(), this);

        modifiedUser = false;
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        buildFragment = new BuildFragment();
        searchFragment = new SearchFragment();
        settingsFragment = new SettingsFragment();

        currentFragment = homeFragment;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_home, currentFragment);
        fragmentTransaction.commit();

    }

    @SuppressLint("ResourceType")
    public void setupNavigationListener() {

        bottomNavigationView.setOnItemSelectedListener(item -> {
            //bottomNavigationView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim));
            switch(item.getItemId()){
                case R.id.homepage:
                    if(modifiedUser == true) {
                        modifiedUser = false;
                        homeFragment = new HomeFragment();
                    }
                    if(currentFragment instanceof HomeFragment) {
                        HomeFragment current = (HomeFragment) currentFragment;
                        current.reload();
                    }
                    else {
                        currentFragment = homeFragment;
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(
                                        R.anim.slide_in, //enter
                                        R.anim.fade_out, //exit
                                        R.anim.fade_in, //popEnter
                                        R.anim.slide_out //popExit
                                )
                                .replace(R.id.container_home, homeFragment).commit();
                    }
                    return true;
                case R.id.profile:
                    if(modifiedUser == true) {
                        modifiedUser = false;
                        profileFragment = new ProfileFragment();
                    }
                    if(currentFragment instanceof ProfileFragment) {
                        ProfileFragment current = (ProfileFragment) currentFragment;
                        current.reload();
                    }
                    else {
                        currentFragment = profileFragment;
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(
                                        R.anim.slide_in, //enter
                                        R.anim.fade_out, //exit
                                        R.anim.fade_in, //popEnter
                                        R.anim.slide_out //popExit
                                )
                                .replace(R.id.container_home, currentFragment).commit();
                    }
                    return true;
                case R.id.create_build:
                    currentFragment = buildFragment;
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in, //enter
                                    R.anim.fade_out, //exit
                                    R.anim.fade_in, //popEnter
                                    R.anim.slide_out //popExit
                            )
                            .replace(R.id.container_home, currentFragment).commit();
                    return true;
                case R.id.search_build:
                    currentFragment = searchFragment;
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in, //enter
                                    R.anim.fade_out, //exit
                                    R.anim.fade_in, //popEnter
                                    R.anim.slide_out //popExit
                            )
                            .replace(R.id.container_home, currentFragment).commit();
                    return true;
                case R.id.settings:
                    currentFragment = settingsFragment;
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in, //enter
                                    R.anim.fade_out, //exit
                                    R.anim.fade_in, //popEnter
                                    R.anim.slide_out //popExit
                            )
                            .replace(R.id.container_home, currentFragment).commit();
                    return true;
            }
            return false;
        });

    }

    public void instantiateColors() {
        Resources.Theme theme = this.getTheme();
        TypedValue typedValue = new TypedValue();

        theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
        color = ColorStateList.valueOf(typedValue.data);

        theme.resolveAttribute(com.google.android.material.R.attr.colorPrimaryDark, typedValue, true);
        colorDark = ColorStateList.valueOf(typedValue.data);

        theme.resolveAttribute(com.google.android.material.R.attr.background, typedValue, true);
        background = ColorStateList.valueOf(typedValue.data);

        theme.resolveAttribute(com.google.android.material.R.attr.backgroundColor, typedValue, true);
        backgroundDark = ColorStateList.valueOf(typedValue.data);

        theme.resolveAttribute(android.R.attr.textColor, typedValue, true);
        textColor = ColorStateList.valueOf(typedValue.data);

        theme.resolveAttribute(android.R.attr.strokeColor, typedValue, true);
        gold = ColorStateList.valueOf(typedValue.data);
    }

    public void setNightMode(){
        if(PcBuilder.isNightMode(this.getApplicationContext()))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public FirebaseUser getUserData() {
        return viewModel.getUserLiveData().getValue();
    }


    @Override
    public void onUserReceived(DocumentSnapshot documentSnapshot) {
        if(user == null)
            user = new User();
        if(documentSnapshot != null) {
            user.updateWithDocument(documentSnapshot);
            viewModel.getUserRepository().downloadBitmapFromFirebaseStorage(user.getUsername(), this);
        }
    }

    @Override
    public void onImageReceived(Bitmap image) {
        if(image != null)
            user.setImage(image);
        else
            user.setImage(User.DEFAULT_IMAGE);

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        buildFragment = new BuildFragment();
        searchFragment = new SearchFragment();
        settingsFragment = new SettingsFragment();

        currentFragment = homeFragment;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container_home, currentFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void recreate() {

        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(getIntent());
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        this.user = ((PcBuilder)getApplication()).getUser();

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        modifiedUser = true;

    }

    public void setInizialUser(User user) {
        this.user = user;


        settingsFragment.setUser(user);

        setupNavigationListener();
    }

    public MainViewModel getViewModel() {
        return this.viewModel;
    }

    public HomeFragment getHomeFragment() {
        return homeFragment;
    }

    public SearchFragment getSearchFragment(){return searchFragment;}
}
