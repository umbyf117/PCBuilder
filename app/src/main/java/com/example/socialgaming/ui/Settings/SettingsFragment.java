package com.example.socialgaming.ui.Settings;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.socialgaming.R;
import com.example.socialgaming.utils.FragmentUtils;
import com.example.socialgaming.view.LoginActivity;

public class SettingsFragment extends Fragment {

    private Switch switchmode;

    public SettingsFragment(){

    }

    private static final int PICK_IMAGE_REQUEST = 1;

    private SettingsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        viewModel = new SettingsViewModel(this.getActivity().getApplication());

        //Switch between Light and Dark mode
        switchmode = view.findViewById(R.id.switch_night_mode);
        switchmode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("night_mode", isChecked);
            editor.apply();

            setNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        });
        //SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        //boolean isNightModeEnabled = sharedPreferences.getBoolean("night_mode", false);
        //switchmode.setChecked(isNightModeEnabled);

        Button logoutButton = view.findViewById(R.id.btnLogout);
        logoutButton.setOnClickListener(view1 -> {
            viewModel.getAuthRepository().logOut();
            FragmentUtils.startActivity((AppCompatActivity) this.getActivity(), new Intent(this.getContext(), LoginActivity.class), true);
        });


        return view;
    }

    private void setNightMode(int i) {
        AppCompatDelegate.setDefaultNightMode(i);
    }

}