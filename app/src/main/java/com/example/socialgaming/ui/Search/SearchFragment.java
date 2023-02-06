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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        sv = view.findViewById(R.id.svSearch);
        lv = view.findViewById(R.id.lvSearch);

        // retrieve list from API
        cpuList = new ArrayList<>();
        String url = "https://rapidapi.com/idirmosh/api/computer-components-api/";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray cpuArray = response.getJSONArray("title");
                            for (int i = 0; i < cpuArray.length(); i++) {
                                cpuList.add(cpuArray.getString(i));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        Volley.newRequestQueue(getContext()).add(request);

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

        List<BuildPc> buildPcList = new ArrayList<>();
        buildPcList.add(new BuildPc("Build 1", "CPU: Intel i7, GPU: Nvidia RTX 3080, RAM: 16 GB"));
        buildPcList.add(new BuildPc("Build 2", "CPU: AMD Ryzen 9, GPU: AMD Radeon RX 6900 XT, RAM: 32 GB"));
        buildPcList.add(new BuildPc("Build 3", "CPU: Intel i5, GPU: Nvidia GTX 1660, RAM: 8 GB"));
        buildPcList.add(new BuildPc("Build 4", "CPU: Intel i5, GPU: Nvidia GTX 1660, RAM: 8 GB"));
        buildPcList.add(new BuildPc("Build 5", "CPU: Intel i5, GPU: Nvidia GTX 1660, RAM: 8 GB"));
        buildPcList.add(new BuildPc("Build 6", "CPU: Intel i5, GPU: Nvidia GTX 1660, RAM: 8 GB"));

        BuildPcAdapter adapter = new BuildPcAdapter(view.getContext(), buildPcList);

        lv = view.findViewById(R.id.lvSearch);
        lv.setAdapter(adapter);


        return view;
    }

    class BuildPc {
        private String title;
        private String details;

        public BuildPc(String title, String details) {
            this.title = title;
            this.details = details;
        }

        public String getTitle() {
            return title;
        }

        public String getDetails() {
            return details;
        }
    }

    class BuildPcAdapter extends ArrayAdapter<BuildPc> {
        public BuildPcAdapter(Context context, List<BuildPc> buildPcList) {
            super(context, 0, buildPcList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BuildPc buildPc = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_bubble, parent, false);
            }

            TextView titleTextView = convertView.findViewById(R.id.nameBuild);
            TextView detailsTextView = convertView.findViewById(R.id.creator);

            titleTextView.setText(buildPc.getTitle());
            detailsTextView.setText(buildPc.getDetails());

            return convertView;
        }

    }
}