package com.example.socialgaming.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.socialgaming.R;
import com.example.socialgaming.databinding.MenuActivityBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;


public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private MenuActivityBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate salva in memoria il menu e permette di aggiungere elementi alla actionbar
        binding = MenuActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appbarMenu.toolbar);
        binding.appbarMenu.floatButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Boh", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigation = binding.navView;

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home,
                 R.id.nav_events, R.id.nav_ranks, R.id.nav_news).setOpenableLayout(drawer).build();
        NavController controller = Navigation.findNavController(this, R.id.navhost_fragment_content_menu);
        NavigationUI.setupActionBarWithNavController(this, controller, appBarConfiguration);
        NavigationUI.setupWithNavController(navigation, controller);

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
