package com.example.socialgaming.ui.profile;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.socialgaming.R;
import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.User;
import com.example.socialgaming.repository.callbacks.IUserCallback;
import com.example.socialgaming.ui.home.HomeFragment;
import com.example.socialgaming.view.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.zip.Inflater;

public class ProfileFragment extends Fragment implements IUserCallback {

    public static final ColorStateList BACKGROUND_LIGHT = ColorStateList.valueOf(Color.parseColor("#CCCCCC"));
    public static final ColorStateList TEXT_LIGHT = ColorStateList.valueOf(Color.parseColor("#252525"));
    public static final ColorStateList BLUE = ColorStateList.valueOf(Color.parseColor("#415a77"));

    private ImageView image;
    private User user;
    private ProfileViewModel profileViewModel;
    private View view;

    private Button createdBuild;
    private Button favoriteBuild;
    private LinearLayout createdBuildLayout;
    private LinearLayout favoriteBuildLayout;
    private ScrollView buildScrollView;
    private LinearLayout containerBuilds;

    private boolean created;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileViewModel = new ProfileViewModel(getActivity().getApplication());

        MainActivity activity = (MainActivity) getActivity();
        activity.setNightMode(AppCompatDelegate.getDefaultNightMode());

        user = new User();
        profileViewModel.getUserRepository().getUserData(
                profileViewModel.getAuthRepository().getUserLiveData().getValue().getDisplayName(),
                this);

        image = view.findViewById(R.id.prof_pic);
        createdBuild = view.findViewById(R.id.createdBuildButton);
        favoriteBuild = view.findViewById(R.id.favoriteBuildButton);
        createdBuildLayout = view.findViewById(R.id.createdBuildBackground);
        favoriteBuildLayout = view.findViewById(R.id.favoriteBuildBackground);
        created = true;

        containerBuilds = view.findViewById(R.id.containerBuilds);


        switchBuildsListener();

        return view;
    }

    public String getUserName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getDisplayName();
    }


    @Override
    public void onUserReceived(DocumentSnapshot documentSnapshot) {
        if(user == null)
            user = new User();
        user.updateWithDocument(documentSnapshot);
        TextView username = view.findViewById(R.id.username);
        username.setText(user.getUsername());
        TextView value = view.findViewById(R.id.userValue);
        value.setText("" + user.getUserValue());

        setCreatedBuilds(this.getLayoutInflater());

        if(user.getImage() != null)
            image.setImageBitmap(user.getImage());
    }

    private void setCreatedBuilds(LayoutInflater inflater) {

        View childLayout = null;

        for (int i = 0; i < user.getCreated().size(); i++) {
            if(i%2 == 0) {
                childLayout = inflater.inflate(R.layout.card_line, containerBuilds, false);
                LinearLayout cardBubbleLayout = childLayout.findViewById(R.id.bubble_left);
                View cardBubble = LayoutInflater.from(childLayout.getContext()).inflate(R.layout.card_bubble, cardBubbleLayout, false);

                cardBubble = getCreatedBuildView(user.getCreated().get(i), cardBubble);

                cardBubbleLayout.addView(cardBubble);
            }
            else {
                LinearLayout cardBubbleLayout = childLayout.findViewById(R.id.bubble_right);
                View cardBubble = LayoutInflater.from(childLayout.getContext()).inflate(R.layout.card_bubble, cardBubbleLayout, false);

                cardBubble = getCreatedBuildView(user.getCreated().get(i), cardBubble);

                cardBubbleLayout.addView(cardBubble);
                containerBuilds.addView(childLayout);
            }
        }

        if(user.getCreated().size() % 2 == 0 && childLayout != null)
            containerBuilds.addView(childLayout);


    }

    private View getCreatedBuildView(Build b, View v) {
        ImageView buildImage = v.findViewById(R.id.imageBuild);
        buildImage.setImageBitmap(b.getImage());
        TextView buildName = v.findViewById(R.id.nameBuild);
        buildName.setText(b.getName());
        TextView value = v.findViewById(R.id.creator);
        value.setText(b.getValue() + "");
        ImageView star = v.findViewById(R.id.saveBuild);
        star.setVisibility(View.INVISIBLE);
        star.setClickable(false);

        return v;
    }

    private void setFavoriteBuilds(LayoutInflater inflater) {

        View childLayout = null;

        for (int i = 0; i < user.getFavorite().size(); i++) {
            if(i%2 == 0) {
                childLayout = inflater.inflate(R.layout.card_line, containerBuilds, false);
                LinearLayout cardBubbleLayout = childLayout.findViewById(R.id.bubble_left);
                View cardBubble = LayoutInflater.from(childLayout.getContext()).inflate(R.layout.card_bubble, cardBubbleLayout, false);

                cardBubble = getFavoriteBuildView(user.getFavorite().get(i), cardBubble);

                cardBubbleLayout.addView(cardBubble);
            }
            else {
                LinearLayout cardBubbleLayout = childLayout.findViewById(R.id.bubble_right);
                View cardBubble = LayoutInflater.from(childLayout.getContext()).inflate(R.layout.card_bubble, cardBubbleLayout, false);

                cardBubble = getFavoriteBuildView(user.getFavorite().get(i), cardBubble);

                cardBubbleLayout.addView(cardBubble);
                containerBuilds.addView(childLayout);
            }
        }

        if(user.getCreated().size() % 2 == 0 && childLayout != null)
            containerBuilds.addView(childLayout);


    }

    private View getFavoriteBuildView(Build b, View v) {
        ImageView buildImage = v.findViewById(R.id.imageBuild);
        buildImage.setImageBitmap(b.getImage());
        TextView buildName = v.findViewById(R.id.nameBuild);
        buildName.setText(b.getName());
        TextView value = v.findViewById(R.id.creator);
        value.setText(b.getCreator().getUsername());

        ImageView star = v.findViewById(R.id.saveBuild);
        star.setOnClickListener(view -> {

            if(user.getFavorite().contains(b)) {
                user.getFavorite().remove(b);
                star.setForegroundTintList(HomeFragment.BLUE_DARK);
            }

            else {
                user.getFavorite().add(b);
                star.setForegroundTintList(HomeFragment.GOLD);
            }

        });

        return v;
    }

    public void switchBuildsListener() {

        createdBuild.setOnClickListener(view -> {
            if(!created) {
                created = true;
                createdBuild.setBackgroundTintList(BLUE);
                favoriteBuild.setBackgroundTintList(BACKGROUND_LIGHT);
                createdBuildLayout.setBackgroundColor(BACKGROUND_LIGHT.getDefaultColor());
                favoriteBuildLayout.setBackgroundColor(BLUE.getDefaultColor());
                createdBuild.setTextColor(BACKGROUND_LIGHT);
                favoriteBuild.setTextColor(TEXT_LIGHT);
                createdBuild.setBackgroundResource(R.drawable.round_bottom);
                favoriteBuild.setBackgroundResource(R.drawable.round_top);

                setCreatedBuilds(this.getLayoutInflater());
            }
        });

        favoriteBuild.setOnClickListener(view -> {
            if(created) {
                created = false;
                favoriteBuild.setBackgroundTintList(BLUE);
                createdBuild.setBackgroundTintList(BACKGROUND_LIGHT);
                favoriteBuildLayout.setBackgroundColor(BACKGROUND_LIGHT.getDefaultColor());
                createdBuildLayout.setBackgroundColor(BLUE.getDefaultColor());
                favoriteBuild.setTextColor(BACKGROUND_LIGHT);
                createdBuild.setTextColor(TEXT_LIGHT);
                favoriteBuild.setBackgroundResource(R.drawable.round_bottom);
                createdBuild.setBackgroundResource(R.drawable.round_top);

                setFavoriteBuilds(this.getLayoutInflater());
            }
        });

    }

}