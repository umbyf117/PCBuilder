package com.example.socialgaming.ui.Search;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.socialgaming.R;
import com.example.socialgaming.api.ComponentsFetchr;

import java.io.IOException;

public class SearchFragment extends Fragment {
    private static final String TAG = "Search Fragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FetchItemsTask().execute();
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            new ComponentsFetchr().fetchItems();
            return null;
        }
    }

}
