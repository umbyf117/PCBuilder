package com.example.socialgaming.ui.Search;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.socialgaming.R;
import com.example.socialgaming.api.ComponentsFetcher;
import com.example.socialgaming.view.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private SearchView sv;
    private ArrayAdapter<String> adapter;
    private ListView lv;


    /*
        // set adapter
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, cpuList);
        lv.setAdapter(adapter);

        // setup search view
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return view;

    }
    */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        MainActivity activity = (MainActivity) getActivity();
        activity.setNightMode(AppCompatDelegate.getDefaultNightMode());
        /*
        BuildPcAdapter adapter = new BuildPcAdapter(view.getContext(), buildPcList);

        lv = view.findViewById(R.id.lvSearch);
        lv.setAdapter(adapter);*/


        return view;
    }

}