package com.example.socialgaming.ui.home;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

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
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.transition.Fade;

import com.example.socialgaming.R;
import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.BuildFirestore;
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

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeFragment extends Fragment implements IUserCallback, IBuildCallback {

    private static final int BUILD_PER_LOAD = 2;

    public static final ColorStateList BLUE_DARK = ColorStateList.valueOf(Color.parseColor("#1b263b"));
    public static final ColorStateList GOLD = ColorStateList.valueOf(Color.parseColor("#FFD700"));
    public static final ColorStateList RED = ColorStateList.valueOf(Color.RED);
    public static final ColorStateList GREEN = ColorStateList.valueOf(Color.GREEN);

    private HomeFragmentViewModel homeViewModel;
    private View currentView;

    private User user;
    private List<BuildFirestore> builds;

    private LinearLayout buildList;
    private TextView username;
    private ImageView image;

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

        homeViewModel.getBuildRepository().getBuildList(BUILD_PER_LOAD, 0, this);

        buildList = currentView.findViewById(R.id.buildLayout);

        user = new User();
        builds = new ArrayList<>();

        username = currentView.findViewById(R.id.username);
        image = currentView.findViewById(R.id.prof_pic);

        if(user != null)
            username.setText(user.getUsername());

        return currentView;
    }

    public void setHomePageBubbles() {

        // Recupera l'istanza della ScrollView
        buildList = currentView.findViewById(R.id.buildLayout);

        // Crea una nuova istanza di LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(buildList.getContext());

        //List<Build> builds = homeViewModel.getBuildRepository().getBuildList(BUILD_PER_LOAD, 0, this);
        if(builds != null)
            for(BuildFirestore b : builds) {
                setBuildBubble(inflater, currentView, b);
            }

    }

    public void setBuildBubble(LayoutInflater inflater, View view, BuildFirestore b) {
        // Inflate il layout incluso (template.xml)
        View templateView = inflater.inflate(R.layout.bubble_template, null);

        // Imposta i parametri richiesti sulla vista inflata
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 16, 0, 16);

        ImageView image = templateView.findViewById(R.id.buildImage);

        TextView name = templateView.findViewById(R.id.nameBuild);
        name.setText(b.getName());

        TextView creator = templateView.findViewById(R.id.creator);
        creator.setText(b.getCreator());

        TextView rate = templateView.findViewById(R.id.value);
        rate.setText(b.getValue() + "");

        if(b.getCreator().equalsIgnoreCase(user.getUsername())) {
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

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createpopupwindow(templateView, b);
            }
        });

        templateView.setLayoutParams(params);
        buildList.addView(templateView);
    }


    private void showPopupWindow(View view){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.card_bubble, null);

        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

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
        BuildFirestore build = new BuildFirestore();
        if(documentSnapshot.exists()) {
            build.updateWithDocument(documentSnapshot);
            builds.add(build);
            homeViewModel.getBuildRepository()
                    .downloadBitmapFromFirebaseStorage(build.getUuid().toString(), build, this);
            setBuildBubble(LayoutInflater.from(buildList.getContext()), currentView, build);
        }
    }

    @Override
    public void onBuildsReceived(List<DocumentSnapshot> documentsSnapshot, boolean created) {
        if(builds == null)
            builds = new ArrayList<>();

        for(DocumentSnapshot d : documentsSnapshot)
            this.onBuildReceived(d);
        //setHomePageBubbles();
    }

    @Override
    public void onImageReceived(Bitmap bitmap, BuildFirestore build) {
        build.setImage(bitmap);
        setBuildBubble(LayoutInflater.from(buildList.getContext()), currentView, build);
    }

    public void createpopupwindow(View templateView, BuildFirestore b) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popupbuild, null);

        TextView nameBuild = popUpView.findViewById(R.id.buildName);
        nameBuild.setText(b.getName());
        TextView motherboard = popUpView.findViewById(R.id.info1);
        motherboard.setText("Motherboard: " + b.getBoardTitle());
        TextView cpu = popUpView.findViewById(R.id.info2);
        cpu.setText("CPU: " + b.getCpuTitle());
        TextView ram = popUpView.findViewById(R.id.info3);
        ram.setText("RAM: " + b.getRamsTitle());
        TextView cpuFan = popUpView.findViewById(R.id.info4);
        cpuFan.setText("CPU Fan: " + b.getFanTitle());
        TextView gpu = popUpView.findViewById(R.id.info5);
        gpu.setText("Graphic Card: " + b.getGpuTitle());
        TextView storage = popUpView.findViewById(R.id.info6);
        storage.setText("Storage: " + b.getMemoriesTitle());
        TextView psu = popUpView.findViewById(R.id.info7);
        psu.setText("Power Supply: " + b.getPsuTitle());
        TextView mcase = popUpView.findViewById(R.id.info8);
        mcase.setText("Case: " + b.getHouseTitle());
        TextView price = popUpView.findViewById(R.id.price);
        price.setText();

        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);
        templateView.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(templateView, Gravity.CENTER, 0, 0);
            }
        });
    }

}
