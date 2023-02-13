package com.example.socialgaming.ui.Search;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.socialgaming.R;
import com.example.socialgaming.data.BuildFirestore;
import com.example.socialgaming.data.User;
import com.example.socialgaming.repository.callbacks.ISearchCallback;
import com.example.socialgaming.ui.home.HomeFragment;
import com.example.socialgaming.view.MainActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements ISearchCallback {

    private EditText searchBuildName;
    private EditText searchAuthorName;
    private Button searchButton;
    private List<BuildFirestore> results;
    private String searchQuery1, searchQuery2;
    private User user;
    private HomeFragment hf;
    private SearchFragment searchFrag;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private int imageLeft;

    private FirebaseFirestore firestore;

    private View currentView;
    private SearchViewModel searchViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchFrag = this;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_search, container, false);
        MainActivity activity = (MainActivity) this.getActivity();
        user = activity.getUser();

        searchViewModel = new SearchViewModel(getActivity().getApplication());

        searchButton = currentView.findViewById(R.id.searchButton);

        firestore = FirebaseFirestore.getInstance();
        results = new ArrayList<>();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchBuildName = currentView.findViewById(R.id.search_build_name);
                searchAuthorName = currentView.findViewById(R.id.search_build_author);
                searchQuery1 = searchBuildName.getText().toString();
                searchQuery2 = searchAuthorName.getText().toString();

                if (searchQuery1.equals("") && searchQuery2.equals("")) return;

                searchViewModel.getBuildRepository().searchBuilds(searchQuery1, searchQuery2, searchFrag);
            }
        });

        return currentView;
    }

    @Override
    public void onSearch(List<DocumentSnapshot> documents) {
        if (documents == null) return;
        List<BuildFirestore> builds = new ArrayList<>();
        for (DocumentSnapshot document : documents) {
            BuildFirestore build = new BuildFirestore();
            build.updateWithDocument(document);
            builds.add(build);
        }

        if(builds.size() == 0)
            Toast.makeText(this.getContext(), "Do not found any builds with that parameters", Toast.LENGTH_SHORT).show();

        else {
            ResultsFragment newFragment = new ResultsFragment();
            newFragment.setBuildFirestores(builds);
            newFragment.setUser(user);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(this.getId(), newFragment).addToBackStack(null).commit();
        }

    }

    @Override
    public void onImageReceived(Bitmap decodeByteArray, BuildFirestore build) {}
}