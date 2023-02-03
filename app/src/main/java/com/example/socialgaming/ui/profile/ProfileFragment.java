package com.example.socialgaming.ui.profile;

import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialgaming.R;
import com.example.socialgaming.databinding.FragmentProfileBinding;
import com.example.socialgaming.ui.Settings.SettingsFragment;
import com.example.socialgaming.utils.FragmentUtils;
import com.example.socialgaming.view.auth.LoginFragment;
import com.example.socialgaming.view.auth.RegisterFragment;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private String userId, userMail;
    private boolean set;
    private ImageView imgbtn;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //get user authentication info
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = getUserName();
        userMail = user.getEmail();

        //display firebase username instead of textview
        TextView tv1 = view.findViewById(R.id.profUser);
        tv1.setText(userId);
        TextView tv2 = view.findViewById(R.id.profMail);
        tv2.setText(userMail);

        return view;
    }


    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public String getUserName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getDisplayName();
    }


}