package com.example.socialgaming.ui.profile;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.socialgaming.R;
import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.BuildFirestore;
import com.example.socialgaming.data.User;
import com.example.socialgaming.repository.callbacks.IBuildCallback;
import com.example.socialgaming.repository.callbacks.IUserCallback;
import com.example.socialgaming.ui.home.HomeFragment;
import com.example.socialgaming.utils.BubbleUtils;
import com.example.socialgaming.view.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements IBuildCallback {

    private MainActivity activity;
    private ImageView image;
    private User user;
    private ProfileViewModel profileViewModel;
    private View view;
    private List<BuildFirestore> created;
    private List<BuildFirestore> favorite;

    private Button createdBuild;
    private Button favoriteBuild;
    private LinearLayout createdBuildLayout;
    private LinearLayout favoriteBuildLayout;
    private LinearLayout createdBuilds;
    private LinearLayout favoriteBuilds;
    private NestedScrollView createdScrollView;
    private NestedScrollView favoriteScrollView;

    private boolean createdPanel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) this.getActivity();
        activity.setNightMode();
        user = activity.getUser();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileViewModel = new ProfileViewModel(getActivity().getApplication());

        image = view.findViewById(R.id.prof_pic);

        createdBuild = view.findViewById(R.id.createdBuildButton);
        favoriteBuild = view.findViewById(R.id.favoriteBuildButton);
        createdBuildLayout = view.findViewById(R.id.createdBuildBackground);
        favoriteBuildLayout = view.findViewById(R.id.favoriteBuildBackground);
        createdScrollView = view.findViewById(R.id.createdScrollView);
        favoriteScrollView = view.findViewById(R.id.favoriteScrollView);
        createdPanel = true;

        createdBuilds = view.findViewById(R.id.containerCreatedBuilds);
        favoriteBuilds = view.findViewById(R.id.containerFavoriteBuilds);

        TextView username = view.findViewById(R.id.username);
        username.setText(user.getUsername());
        TextView value = view.findViewById(R.id.userValue);
        value.setText("Builds: " + user.getCreated().size());

        profileViewModel.getBuildRepository().getUserBuilds(user.getCreated(), this, true);
        profileViewModel.getBuildRepository().getUserBuilds(user.getFavorite(), this, false);

        if (user.getImage() != null)
            image.setImageBitmap(user.getImage());

        switchBuildsListener();

        return view;
    }

    public String getUserName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getDisplayName();
    }

    public void switchBuildsListener() {

        createdBuild.setOnClickListener(view -> {
            if (!createdPanel) {
                createdPanel = true;
                favoriteBuild.setBackgroundTintList(activity.color);
                createdBuild.setBackgroundTintList(activity.background);
                favoriteBuildLayout.setBackgroundColor(activity.background.getDefaultColor());
                createdBuildLayout.setBackgroundColor(activity.color.getDefaultColor());
                favoriteBuild.setTextColor(activity.background);
                createdBuild.setTextColor(activity.textColor);
                favoriteBuild.setBackgroundResource(R.drawable.round_bottom);
                createdBuild.setBackgroundResource(R.drawable.round_top);

                createdScrollView.setVisibility(View.VISIBLE);
                createdScrollView.setFocusable(true);
                createdScrollView.setClickable(true);
                favoriteScrollView.setVisibility(View.GONE);
                favoriteScrollView.setFocusable(false);
                favoriteScrollView.setClickable(false);
            }
        });

        favoriteBuild.setOnClickListener(view -> {
            if (createdPanel) {
                createdPanel = false;
                createdBuild.setBackgroundTintList(activity.color);
                favoriteBuild.setBackgroundTintList(activity.background);
                createdBuildLayout.setBackgroundColor(activity.background.getDefaultColor());
                favoriteBuildLayout.setBackgroundColor(activity.color.getDefaultColor());
                createdBuild.setTextColor(activity.background);
                favoriteBuild.setTextColor(activity.textColor);
                createdBuild.setBackgroundResource(R.drawable.round_bottom);
                favoriteBuild.setBackgroundResource(R.drawable.round_top);

                favoriteScrollView.setVisibility(View.VISIBLE);
                favoriteScrollView.setFocusable(true);
                favoriteScrollView.setClickable(true);
                createdScrollView.setVisibility(View.GONE);
                createdScrollView.setFocusable(false);
                createdScrollView.setClickable(false);
            }
        });

    }

    @Override
    public void onBuildReceived(DocumentSnapshot documentSnapshot, boolean created) {
        if (documentSnapshot == null)
            return;

        if (this.created == null)
            this.created = new ArrayList<>();

        if (favorite == null)
            favorite = new ArrayList<>();

        BuildFirestore buildFirestore = new BuildFirestore();
        buildFirestore.updateWithDocument(documentSnapshot);
        profileViewModel.getBuildRepository().downloadBitmapFromFirebaseStorage(
                buildFirestore.getUuid().toString(), buildFirestore, this, created);
    }

    @Override
    public void onBuildsReceived(List<DocumentSnapshot> documentsSnapshot, boolean created) {

    }

    @Override
    public void onImageReceived(Bitmap bitmap, BuildFirestore build, boolean created) {
        build.setImage(bitmap);
        if(created) {
            this.created.add(build);
            BubbleUtils.setBuildBubble(build, user, this, createdBuilds);
        }
        else {
            favorite.add(build);
            BubbleUtils.setBuildBubble(build, user, this, favoriteBuilds);
        }
    }

    public void reload() {
        created = new ArrayList<>();
        favorite = new ArrayList<>();

        profileViewModel.getBuildRepository().getUserBuilds(user.getCreated(), this, true);
        profileViewModel.getBuildRepository().getUserBuilds(user.getFavorite(), this, false);

        createdBuildLayout.removeAllViewsInLayout();
        favoriteBuildLayout.removeAllViewsInLayout();
    }

    public ProfileViewModel getProfileViewModel() {
        return profileViewModel;
    }
}