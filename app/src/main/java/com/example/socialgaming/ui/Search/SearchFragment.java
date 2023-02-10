package com.example.socialgaming.ui.Search;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.socialgaming.R;
import com.example.socialgaming.api.ComponentsFetcher;
import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.BuildFirestore;
import com.example.socialgaming.data.User;
import com.example.socialgaming.repository.callbacks.IBuildCallback;
import com.example.socialgaming.repository.callbacks.ISearchCallback;
import com.example.socialgaming.repository.component.BuildRepository;
import com.example.socialgaming.ui.home.HomeFragment;
import com.example.socialgaming.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SearchFragment extends Fragment implements ISearchCallback {

    private EditText searchBuildName;
    private EditText searchAuthorName;
    private Button searchButton;
    private List<BuildFirestore> results;
    private String searchQuery1, searchQuery2;
    private BuildRepository br;
    private User user;

    private FirebaseFirestore firestore;

    private View currentView;
    private SearchViewModel searchViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_search, container, false);
        MainActivity activity = (MainActivity) this.getActivity();
        user = activity.getUser();
        activity.setNightMode(AppCompatDelegate.getDefaultNightMode());

        searchViewModel = new SearchViewModel(getActivity().getApplication());
        searchViewModel.getBuildRepository().getBuildList(10, 0, this);

        searchButton = currentView.findViewById(R.id.searchButton);

        firestore = FirebaseFirestore.getInstance();

        results = new ArrayList<>();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchBuildName = currentView.findViewById(R.id.search_build_name);
                searchAuthorName = currentView.findViewById(R.id.search_build_author);
                searchQuery1 = searchBuildName.getText().toString().toLowerCase();
                searchQuery2 = searchAuthorName.getText().toString().toLowerCase();

                if(isEmptyName()){
                    firestore.collection("builds")
                            .whereEqualTo("creator", searchAuthorName)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                                    List<CardView> cardViews = new ArrayList<>();
                                    for(DocumentSnapshot document : documents){
                                        String title = document.getString("creator");
                                        CardView cardView = new CardView(getContext());

                                        cardViews.add(cardView);
                                    }
                                    ResultsFragment newFragment = new ResultsFragment();
                                    Bundle args = new Bundle();
                                    args.putSerializable("card_views", (Serializable) cardViews);
                                    newFragment.setArguments(args);
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.search_fragment, newFragment);
                                    transaction.commit();
                                }
                            });
                }
                else if(isEmptyAuthor()){
                    firestore.collection("builds")
                            .whereEqualTo("name", searchBuildName)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                                    List<CardView> cardViews = new ArrayList<>();
                                    for(DocumentSnapshot document : documents){
                                        String title = document.getString("name");
                                        CardView cardView = new CardView(getContext());
                                        cardViews.add(cardView);
                                    }
                                    ResultsFragment newFragment = new ResultsFragment();
                                    Bundle args = new Bundle();
                                    args.putSerializable("card_views", (Serializable) cardViews);
                                    newFragment.setArguments(args);
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.search_fragment, newFragment);
                                    transaction.commit();
                                }
                            });
                }
                else if(!isEmptyName() && !isEmptyAuthor()){

                }
            }
        });

        return currentView;
    }

    private boolean isEmptyName(){
        if(searchBuildName.toString().equals("") || searchBuildName.toString().equals(null)) return true;
        else return false;
    }

    private boolean isEmptyAuthor(){
        if(searchAuthorName.toString().equals("") || searchAuthorName.toString() == null) return true;
        else return false;
    }

    private void switchToBuild(Build build){

    }

    @Override
    public void onSearch(List<DocumentSnapshot> documents) {
        if(documents == null) return;
        List<BuildFirestore> builds = new ArrayList<>();
        for(DocumentSnapshot document : documents){
            BuildFirestore build = new BuildFirestore();
            build.updateWithDocument(document);
            builds.add(build);
        }
        ResultsFragment newFragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putSerializable("documents", documents);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.search_fragment, newFragment);
        transaction.commit();
    }
}