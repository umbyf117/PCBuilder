package com.example.socialgaming.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.socialgaming.R;
import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.User;
import com.example.socialgaming.databinding.FragmentHomeBinding;
import com.example.socialgaming.repository.component.BuildRepository;

public class HomeFragment extends Fragment {

    private static final int BUILD_PER_LOAD = 10;

    private FragmentHomeBinding binding;
    private HomeFragmentViewModel homeViewModel;

    private User user;
    private LinearLayout buildList;
    private BuildRepository buildRepository;

    public static HomeFragment newInstance(User user) {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.user = user;
        return homeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        homeViewModel = new HomeFragmentViewModel();
        buildRepository = new BuildRepository();

        setHomePageBubbles();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setHomePageBubbles() {

        View view = binding.getRoot();

        // Recupera l'istanza della ScrollView
        buildList = view.findViewById(R.id.buildLayout);

        // Crea una nuova istanza di LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(buildList.getContext());

        for(Build b : buildRepository.getBuildList(50, 0)) {
            setBuildBubble(inflater, view, b);
        }

    }

    public void setBuildBubble(LayoutInflater inflater, View view, Build b) {
        // Inflate il layout incluso (template.xml)
        View templateView = inflater.from(view.getContext()).inflate(R.layout.bubble_template, buildList, false);

        // Imposta i parametri richiesti sulla vista inflata
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 16, 0, 0);

        TextView name = templateView.findViewById(R.id.nameBuild);
        name.setText(b.getName());

        TextView creator = templateView.findViewById(R.id.creator);
        creator.setText(b.getCreator().getUsername());

        TextView rate = templateView.findViewById(R.id.value);
        rate.setText(b.getValue() + "");

        if(b.getCreator().getUsername().equals(user.getUsername())) {
            ImageView save = templateView.findViewById(R.id.saveBuild);
            save.setVisibility(View.INVISIBLE);
            save.setClickable(false);

            ImageView like = templateView.findViewById(R.id.like);
            like.setVisibility(View.INVISIBLE);
            like.setClickable(false);

            ImageView dislike = templateView.findViewById(R.id.dislike);
            dislike.setVisibility(View.INVISIBLE);
            dislike.setClickable(false);
        }

        if(b.getLike().contains(user.getUsername())) {
            ImageView like = templateView.findViewById(R.id.like);
            //like.setForegroundTintList();
        }


        templateView.setLayoutParams(params);
        buildList.addView(templateView);
    }

}
