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

import com.example.socialgaming.R;
import com.example.socialgaming.ui.profile.OnFragmentInteractionListener;

public class SettingsFragment extends Fragment {

    private OnFragmentInteractionListener listener;

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
        ImageButton imgbtn = (ImageButton) view.findViewById(R.id.btnProf);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeFragment(1);
            }
        });

        return view;
    }

}
