package com.example.socialgaming.ui.profile;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
    private FirebaseDatabase fd;
    private DatabaseReference ref;
    private FirebaseUser userFB;
    private String userId, userMail;
    private TextView tv1, tv2;
    private ImageButton imgbtn;
    private OnFragmentInteractionListener listener;

    public ProfileFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //get user authentication info
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = getUserName();
        userMail = user.getEmail();

        //button to settings fragment
        ImageButton imgbtn = root.findViewById(R.id.btnSettings);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeFragment(2);
            }
        });


        //display firebase username instead of textview
        TextView tv1 = root.findViewById(R.id.profUser);
        tv1.setText(userId);
        TextView tv2 = root.findViewById(R.id.profMail);
        tv2.setText(userMail);

        return root;
    }

    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }

    public String getUserName(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getDisplayName();
    }

}
