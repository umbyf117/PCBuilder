package com.example.socialgaming.ui.Search;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.socialgaming.R;
import com.example.socialgaming.data.BuildFirestore;
import com.example.socialgaming.data.User;
import com.example.socialgaming.repository.callbacks.IBuildCallback;
import com.example.socialgaming.utils.BubbleUtils;
import com.example.socialgaming.view.MainActivity;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class ResultsFragment extends Fragment {

    private List<BuildFirestore> buildFirestores;
    private User user;
    private View currentView;
    private LinearLayout buildsContainer;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        currentView = inflater.inflate(R.layout.fragment_results, container, false);

        user = ((MainActivity) getActivity()).getUser();
        buildsContainer = currentView.findViewById(R.id.buildLayout);
        for(BuildFirestore build : buildFirestores)
            BubbleUtils.setBuildBubble(build, user, this, buildsContainer);

        return currentView;

    }

    public List<BuildFirestore> getBuildFirestores() {
        return buildFirestores;
    }

    public void setBuildFirestores(List<BuildFirestore> buildFirestores) {
        this.buildFirestores = buildFirestores;
    }
}
