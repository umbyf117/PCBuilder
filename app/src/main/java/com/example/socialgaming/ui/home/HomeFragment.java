package com.example.socialgaming.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.socialgaming.data.User;
import com.example.socialgaming.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private static final int BUILD_PER_LOAD = 10;

    private FragmentHomeBinding binding;
    private HomeFragmentViewModel homeViewModel;

    private User user;
    private LinearLayout buildList;

    public static HomeFragment newInstance(User user) {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.user = user;
        return homeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        homeViewModel = new HomeFragmentViewModel();

        /*
        // Recupera l'istanza della ScrollView
        ScrollView scrollView = view.findViewById(R.id.buildList);
        buildList = scrollView.findViewById(R.id.buildLayout);

        // Crea una nuova istanza di LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(buildList.getContext());

        // Inflate il layout incluso (template.xml)
        View templateView = inflater.inflate(R.layout.bubble_template, scrollView, false);

        // Imposta i parametri richiesti sulla vista inflata
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 16, 8, 0);
        templateView.setLayoutParams(params);

        // Aggiungi la vista inflata alla ScrollView
        scrollView.addView(templateView);
        */

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
