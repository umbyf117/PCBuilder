package com.example.socialgaming.ui.Search;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.socialgaming.R;
import com.example.socialgaming.data.BuildFirestore;
import com.example.socialgaming.data.User;
import com.example.socialgaming.repository.callbacks.IBuildCallback;
import com.example.socialgaming.repository.callbacks.ISearchCallback;
import com.example.socialgaming.repository.component.BuildRepository;
import com.example.socialgaming.utils.BubbleUtils;
import com.example.socialgaming.utils.FragmentUtils;
import com.example.socialgaming.view.MainActivity;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class ResultsFragment extends Fragment implements ISearchCallback {

    private List<BuildFirestore> buildFirestores;
    private User user;
    private View currentView;
    private LinearLayout buildsContainer;
    private FragmentManager fm;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                destroy();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        currentView = inflater.inflate(R.layout.fragment_results, container, false);

        fm = getActivity().getSupportFragmentManager();
        if(user == null)
            user = ((MainActivity) getActivity()).getUser();

        TextView username = currentView.findViewById(R.id.username);
        username.setText(user.getUsername());
        ImageView image = currentView.findViewById(R.id.prof_pic);
        image.setImageBitmap(user.getImage());

        buildsContainer = currentView.findViewById(R.id.buildLayout);
        for(BuildFirestore build : buildFirestores) {
            new BuildRepository().downloadBitmapFromFirebaseStorage(build.getUuid().toString(), build, this);
        }

        return currentView;
    }

    public void destroy(){
        MainActivity activity = (MainActivity) getActivity();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(this.getId(), activity.getSearchFragment());
        ft.commit();
    }

    public void setBuildFirestores(List<BuildFirestore> buildFirestores) {
        this.buildFirestores = buildFirestores;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void onSearch(List<DocumentSnapshot> documents) {

    }

    @Override
    public void onImageReceived(Bitmap decodeByteArray, BuildFirestore build) {
        build.setImage(decodeByteArray);
        BubbleUtils.setBuildBubble(build, user, this, buildsContainer);
    }
}
