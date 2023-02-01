package com.example.socialgaming.ui.logout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialgaming.R;
import com.example.socialgaming.databinding.FragmentLogoutBinding;
import com.example.socialgaming.view.HomeActivity;
import com.example.socialgaming.view.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutFragment extends Fragment {


    private FragmentLogoutBinding binding;
    private Button logoutyes;
    private Button logoutno;
    private Intent login;
    private Intent home;
    FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentLogoutBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();

        logoutyes = (Button) view.findViewById(R.id.logout_yes);
        logoutyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                singOutUser();
                //login = new Intent(view.getContext(), LoginActivity.class);
                //view.getContext().startActivity(login);
            }
        });
        logoutno = (Button) view.findViewById(R.id.logout_no);
        logoutno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home = new Intent(getActivity(), HomeActivity.class);
                startActivity(home);
            }
        });
    }

    //METODO USATO SOPRA
    private void singOutUser() {
        Intent login = new Intent (getActivity(), LoginActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(login);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        return view;
    }



}
