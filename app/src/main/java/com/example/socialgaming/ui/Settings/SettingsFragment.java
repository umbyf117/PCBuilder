package com.example.socialgaming.ui.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.socialgaming.R;
import com.example.socialgaming.ui.profile.OnFragmentInteractionListener;
import com.example.socialgaming.ui.profile.ProfileFragment;
import com.example.socialgaming.view.MainActivity;

public class SettingsFragment extends Fragment {

    private androidx.appcompat.widget.SwitchCompat switchCompat;

    public SettingsFragment(){

    }

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImageView;
    private Button chooseImageButton;
    private Bitmap profileImage;
    private ImageButton imgbtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        MainActivity activity = (MainActivity) getActivity();
        activity.setNightMode(AppCompatDelegate.getDefaultNightMode());
        switchCompat = view.findViewById(R.id.switch_night_mode);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("night_mode", isChecked);
                editor.apply();

                setNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

            }
        });
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean isNightModeEnabled = sharedPreferences.getBoolean("night_mode", false);
        switchCompat.setChecked(isNightModeEnabled);

        return view;
    }

    private void setNightMode(int i) {
        AppCompatDelegate.setDefaultNightMode(i);
    }

}