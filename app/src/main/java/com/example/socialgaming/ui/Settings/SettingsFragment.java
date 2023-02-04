package com.example.socialgaming.ui.Settings;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.socialgaming.R;
import com.example.socialgaming.ui.profile.OnFragmentInteractionListener;
import com.example.socialgaming.ui.profile.ProfileFragment;

public class SettingsFragment extends Fragment {


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

        return view;
    }

}