package com.example.socialgaming.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.socialgaming.PcBuilder;
import com.example.socialgaming.R;
import com.example.socialgaming.databinding.MenuActivityBinding;
import com.example.socialgaming.repository.user.ReadWriteUserDetails;
import com.example.socialgaming.utils.FragmentUtils;
import com.example.socialgaming.view.model.HomeViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;


public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private MenuActivityBinding binding;
    private HomeViewModel viewModel;
    private FragmentManager fragmentManager;

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

        setSupportActionBar(binding.appbarMenu.toolbar);
        binding.appbarMenu.floatButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Boh", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigation = binding.navView;

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home,
                 R.id.nav_events, R.id.nav_ranks, R.id.nav_news)
                .setOpenableLayout(drawer)
                .build();
        NavController controller = Navigation.findNavController(this, R.id.navhost_fragment_content_menu);
        NavigationUI.setupActionBarWithNavController(this, controller, appBarConfiguration);
        NavigationUI.setupWithNavController(navigation, controller);

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
