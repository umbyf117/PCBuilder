package com.example.socialgaming.ui.home;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.socialgaming.R;
import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.User;
import com.example.socialgaming.databinding.FragmentHomeBinding;
import com.example.socialgaming.repository.callbacks.IBuildCallback;
import com.example.socialgaming.repository.callbacks.IUserCallback;
import com.example.socialgaming.repository.component.BuildRepository;
import com.example.socialgaming.repository.user.AuthRepository;
import com.example.socialgaming.ui.Search.SearchFragment;
import com.example.socialgaming.view.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements IUserCallback, IBuildCallback {

    private static final int BUILD_PER_LOAD = 50;

    public static final ColorStateList BLUE_DARK = ColorStateList.valueOf(Color.parseColor("#1b263b"));
    public static final ColorStateList GOLD = ColorStateList.valueOf(Color.parseColor("#FFD700"));
    public static final ColorStateList RED = ColorStateList.valueOf(Color.RED);
    public static final ColorStateList GREEN = ColorStateList.valueOf(Color.GREEN);

    private HomeFragmentViewModel homeViewModel;
    private View currentView;

    private User user;
    private List<Build> builds;

    private LinearLayout buildList;
    private TextView username;
    private ImageView image;
    private ListView lv;

    public static HomeFragment newInstance(User user) {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.user = user;
        return homeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel = new HomeFragmentViewModel(getActivity().getApplication());

        if(homeViewModel.getAuthRepository().getUserLiveData().getValue().getDisplayName().isEmpty())
            homeViewModel.getAuthRepository().logOut();
        else
            homeViewModel.getUserRepository().getUserData(
                    homeViewModel.getAuthRepository().getUserLiveData().getValue().getDisplayName(),
                    this);

        user = new User();
        builds = new ArrayList<>();

        username = currentView.findViewById(R.id.username);
        image = currentView.findViewById(R.id.prof_pic);
        image.setOnClickListener(v -> {
            //requestStoragePermission();
            //pickImageFromGallery();
        });

        if(user != null)
            username.setText(user.getUsername());

        List<BuildPc> buildPcList = new ArrayList<>();
        buildPcList.add(new BuildPc("Build 1", "CPU: Intel i7, GPU: Nvidia RTX 3080, RAM: 16 GB"));
        buildPcList.add(new BuildPc("Build 2", "CPU: AMD Ryzen 9, GPU: AMD Radeon RX 6900 XT, RAM: 32 GB"));
        buildPcList.add(new BuildPc("Build 3", "CPU: Intel i5, GPU: Nvidia GTX 1660, RAM: 8 GB"));
        buildPcList.add(new BuildPc("Build 4", "CPU: Intel i5, GPU: Nvidia GTX 1660, RAM: 8 GB"));
        buildPcList.add(new BuildPc("Build 5", "CPU: Intel i5, GPU: Nvidia GTX 1660, RAM: 8 GB"));
        buildPcList.add(new BuildPc("Build 6", "CPU: Intel i5, GPU: Nvidia GTX 1660, RAM: 8 GB"));

        BuildPcAdapter adapter = new BuildPcAdapter(currentView.getContext(), buildPcList);

        lv = currentView.findViewById(R.id.lvHome);
        lv.setAdapter(adapter);

        return currentView;
    }

    public void setHomePageBubbles() {

        // Recupera l'istanza della ScrollView
        buildList = currentView.findViewById(R.id.buildLayout);

        // Crea una nuova istanza di LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(buildList.getContext());

        //List<Build> builds = homeViewModel.getBuildRepository().getBuildList(BUILD_PER_LOAD, 0, this);
        if(builds != null)
            for(Build b : builds) {
                setBuildBubble(inflater, currentView, b);
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
        creator.setText(b.getCreator());

        TextView rate = templateView.findViewById(R.id.value);
        rate.setText(b.getValue() + "");

        if(b.getCreator().equals(user.getUsername())) {
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

        else {
            ImageView like = templateView.findViewById(R.id.like);
            ImageView dislike = templateView.findViewById(R.id.dislike);
            ImageView star = templateView.findViewById(R.id.saveBuild);

            if (b.getLike().contains(user.getUsername()))
                like.setForegroundTintList(GREEN);
            else if (b.getDislike().contains(user.getUsername()))
                dislike.setForegroundTintList(RED);
            if (user.getFavorite().contains(b))
                star.setForegroundTintList(GOLD);

            like.setOnClickListener(v -> {
                if(b.getLike().contains(user.getUsername())) {
                    b.getLike().remove(user.getUsername());
                    like.setForegroundTintList(BLUE_DARK);
                }
                else {
                    b.getLike().add(user.getUsername());
                    like.setForegroundTintList(GREEN);
                }
            });

            dislike.setOnClickListener(v -> {
                if(b.getDislike().contains(user.getUsername())) {
                    b.getDislike().remove(user.getUsername());
                    dislike.setForegroundTintList(BLUE_DARK);
                }
                else {
                    b.getDislike().add(user.getUsername());
                    dislike.setForegroundTintList(RED);
                }
            });

            star.setOnClickListener(v -> {
                if(user.getFavorite().contains(b.getUuid().toString())) {
                    user.getFavorite().remove(b.getUuid().toString());
                    star.setForegroundTintList(BLUE_DARK);
                }
                else {
                    user.getFavorite().add(b.getUuid().toString());
                    star.setForegroundTintList(GOLD);
                }
            });

        }


        templateView.setLayoutParams(params);
        buildList.addView(templateView);
    }



    @Override
    public void onUserReceived(DocumentSnapshot documentSnapshot) {
        if(user == null)
            user = new User();
        user.updateWithDocument(documentSnapshot);
        username.setText(user.getUsername());
        image.setImageBitmap(user.getImage());

    }

    @Override
    public void onBuildReceived(DocumentSnapshot documentSnapshot) {
        Build build = new Build();
        if(documentSnapshot.exists()) {
            build.updateWithDocument(documentSnapshot);
            builds.add(build);
        }
    }

    @Override
    public void onBuildsReceived(List<DocumentSnapshot> documentsSnapshot, boolean created) {
        if(builds == null)
            builds = new ArrayList<>();

        for(DocumentSnapshot d : documentsSnapshot)
            this.onBuildReceived(d);
        setHomePageBubbles();
    }

    class BuildPc {
        private String title;
        private String details;

        public BuildPc(String title, String details) {
            this.title = title;
            this.details = details;
        }

        public String getTitle() {
            return title;
        }

        public String getDetails() {
            return details;
        }
    }

    class BuildPcAdapter extends ArrayAdapter<BuildPc> {
        public BuildPcAdapter(Context context, List<BuildPc> buildPcList) {
            super(context, 0, buildPcList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BuildPc buildPc = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_bubble, parent, false);
            }

            TextView titleTextView = convertView.findViewById(R.id.nameBuild);
            TextView detailsTextView = convertView.findViewById(R.id.creator);

            titleTextView.setText(buildPc.getTitle());
            detailsTextView.setText(buildPc.getDetails());

            return convertView;
        }

    }

}
