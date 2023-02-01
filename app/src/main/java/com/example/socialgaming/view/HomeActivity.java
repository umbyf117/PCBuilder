package com.example.socialgaming.view;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.socialgaming.R;
import com.example.socialgaming.data.User;
import com.example.socialgaming.databinding.MenuActivityBinding;
import com.example.socialgaming.ui.profile.ProfileFragment;
import com.example.socialgaming.utils.BuildUtils;
import com.example.socialgaming.utils.FragmentUtils;
import com.example.socialgaming.view.model.HomeViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;


public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private MenuActivityBinding binding;
    private HomeViewModel viewModel;
    private FragmentManager fragmentManager;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_home);

        viewModel.getUserLiveData().observe(this, firebaseUser -> {
            if(firebaseUser == null)
                FragmentUtils.startActivity(this, new Intent(HomeActivity.this, LoginActivity.class), true);
        });


        //inflate salva in memoria il menu e permette di aggiungere elementi alla actionbar
        binding = MenuActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //bottone in basso a destra
        setSupportActionBar(binding.appbarMenu.toolbar);
        binding.appbarMenu.floatButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment pf = new ProfileFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.drawerLayout, pf);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigation = binding.navView;

        appBarConfiguration = new AppBarConfiguration.Builder(
                 R.id.nav_home, R.id.nav_build, R.id.nav_search, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController controller = Navigation.findNavController(this, R.id.navhost_fragment_content_menu);
        NavigationUI.setupActionBarWithNavController(this, controller, appBarConfiguration);
        NavigationUI.setupWithNavController(navigation, controller);

        navigation.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            viewModel.logout();
            return true;
        });

    }

    /*public void onDataChange(@NonNull DataSnapshot snapshot){
        ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
        if(readWriteUserDetails != null){

        }
    }*/

    public FirebaseUser getUserData() {
        return viewModel.getUserLiveData().getValue();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        NavController controller = Navigation.findNavController(this, R.id.navhost_fragment_content_menu);
        return NavigationUI.navigateUp(controller, appBarConfiguration) || super.onSupportNavigateUp();
    }

}
