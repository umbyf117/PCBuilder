package com.example.socialgaming.ui.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.socialgaming.R;
import com.example.socialgaming.data.User;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;


public class ResultsFragment extends Fragment {

    private List<DocumentSnapshot> documents;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if(args != null){
            documents = args.getSerializable("")
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_results, container, false);


        return view;

    }

}
