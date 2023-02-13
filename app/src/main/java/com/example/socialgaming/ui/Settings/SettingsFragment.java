package com.example.socialgaming.ui.Settings;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.example.socialgaming.PcBuilder;
import com.example.socialgaming.R;
import com.example.socialgaming.data.User;
import com.example.socialgaming.utils.FragmentUtils;
import com.example.socialgaming.utils.StringUtils;
import com.example.socialgaming.view.LoginActivity;
import com.example.socialgaming.view.MainActivity;

public class SettingsFragment extends Fragment {

    private View currentView;
    private androidx.appcompat.widget.SwitchCompat switchmode;
    private SettingsViewModel viewModel;
    private User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        //TransitionInflater inflater = TransitionInflater.from(requireContext());
        //setExitTransition(inflater.inflateTransition(R.transition.fade));
    }

    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        currentView = inflater.inflate(R.layout.fragment_settings, container, false);
        viewModel = new SettingsViewModel(this.getActivity().getApplication());
        user = ((MainActivity)this.getActivity()).getUser();

        TextView username = currentView.findViewById(R.id.username);
        username.setText(user.getUsername());

        ImageView image = currentView.findViewById(R.id.prof_pic);
        image.setImageBitmap(user.getImage());

        setupNightMode();
        setupChangePassword();
        setupLogoutButton();

        return currentView;
    }

    public void setupNightMode() {
        switchmode = currentView.findViewById(R.id.switch_night_mode);
        if(PcBuilder.getNightMode(this.getActivity().getApplicationContext()))
            switchmode.setChecked(true);
        switchmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isDarkMode", isChecked);
                editor.apply();
            }
        });

        this.getActivity().recreate();

    }

    public void setupChangePassword() {

        EditText oldPassword = currentView.findViewById(R.id.txtOldPassword);
        EditText newPassword = currentView.findViewById(R.id.txtNewPassword);
        EditText confirmPassword = currentView.findViewById(R.id.txtConfirmPassword);
        Button confirm = currentView.findViewById(R.id.confirmChange);

        confirm.setOnClickListener(view -> {
            String old = oldPassword.getText().toString();
            String pass = newPassword.getText().toString();
            String confirm1 = confirmPassword.getText().toString();
            if(old.equals(user.getPassword())) {
                if(pass.equals(confirm1) && StringUtils.checkPassword(pass)) {
                    viewModel.getUserRepository().updatePassword(user, pass, getContext(), viewModel.getAuthRepository());
                    oldPassword.setText("");
                    newPassword.setText("");
                    confirmPassword.setText("");
                }
                else if (!StringUtils.checkPassword(pass)){
                    Toast.makeText(getContext(), "Passwords must be at least 8 characters long!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getContext(), "The old password does not match with your actual one!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setupLogoutButton() {
        Button logoutButton = currentView.findViewById(R.id.btnLogout);
        logoutButton.setOnClickListener(view1 -> {
            viewModel.getAuthRepository().logOut();
            FragmentUtils.startActivity((AppCompatActivity) this.getActivity(), new Intent(this.getContext(), LoginActivity.class), true);
        });
    }

}