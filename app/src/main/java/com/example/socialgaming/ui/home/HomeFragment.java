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
import android.transition.TransitionInflater;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.transition.Fade;

import com.example.socialgaming.PcBuilder;
import com.example.socialgaming.R;
import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.BuildFirestore;
import com.example.socialgaming.data.User;
import com.example.socialgaming.repository.callbacks.IBuildCallback;
import com.example.socialgaming.repository.callbacks.IUserCallback;
import com.example.socialgaming.repository.component.BuildRepository;
import com.example.socialgaming.repository.user.AuthRepository;
import com.example.socialgaming.ui.Search.SearchFragment;
import com.example.socialgaming.utils.BubbleUtils;
import com.example.socialgaming.view.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressLint("ResourceType")
public class HomeFragment extends Fragment implements IUserCallback, IBuildCallback {

    private static final int BUILD_PER_LOAD = 10;

    private MainActivity activity;

    private HomeFragmentViewModel homeViewModel;
    private View currentView;

    private User user;
    private List<BuildFirestore> builds;

    private LinearLayout buildList;
    private TextView username;
    private ImageView image;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TransitionInflater inflater = TransitionInflater.from(requireContext());
        //setExitTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) this.getActivity();
        homeViewModel = new HomeFragmentViewModel(activity.getApplication());

        if(activity.getUser() != null)
            user = activity.getUser();
        else
            homeViewModel.getUserRepository().getUserData(
                    homeViewModel.getAuthRepository().getUserLiveData().getValue().getDisplayName(), this);

        currentView = inflater.inflate(R.layout.fragment_home, container, false);

        return currentView;
    }

    @Override
    public void onBuildReceived(DocumentSnapshot documentSnapshot, boolean created) {
        BuildFirestore build = new BuildFirestore();
        if (documentSnapshot.exists()) {
            build.updateWithDocument(documentSnapshot);
            builds.add(build);
            homeViewModel.getBuildRepository()
                    .downloadBitmapFromFirebaseStorage(build.getUuid().toString(), build, this, false);
        }
    }

    @Override
    public void onBuildsReceived(List<DocumentSnapshot> documentsSnapshot, boolean created) {
        if (builds == null)
            builds = new ArrayList<>();

        for (DocumentSnapshot d : documentsSnapshot)
            this.onBuildReceived(d, created);
    }

    @Override
    public void onImageReceived(Bitmap bitmap, BuildFirestore build, boolean created) {
        int dimension = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap,
                (bitmap.getWidth() - dimension) / 2,
                (bitmap.getHeight() - dimension) / 2,
                dimension, dimension);
        build.setImage(croppedBitmap);
        BubbleUtils.setBuildBubble(build, user, this, buildList);

    }

    @Override
    public void onUserReceived(DocumentSnapshot documentSnapshot) {
        if (user == null)
            user = new User();
        if (documentSnapshot != null) {
            user.updateWithDocument(documentSnapshot);
            homeViewModel.getUserRepository().downloadBitmapFromFirebaseStorage(user.getUsername(), this);
        }

    }

    @Override
    public void onImageReceived(Bitmap image) {
        if (image != null)
            user.setImage(image);
        else
            user.setImage(User.DEFAULT_IMAGE);

        activity.setInizialUser(user);
        PcBuilder application = (PcBuilder) activity.getApplication();
        application.setUser(user);

        buildList = currentView.findViewById(R.id.buildLayout);

        builds = new ArrayList<>();

        username = currentView.findViewById(R.id.username);
        username.setText(user.getUsername());

        this.image = currentView.findViewById(R.id.prof_pic);
        this.image.setImageBitmap(user.getImage());

        homeViewModel.getBuildRepository().getBuildList(BUILD_PER_LOAD, 0, this);

    }

    public void reload() {
        builds = new ArrayList<>();
        homeViewModel.getBuildRepository().getBuildList(BUILD_PER_LOAD, 0, this);
        buildList.removeAllViewsInLayout();
    }

    public User getUser() {
        return user;
    }
}
