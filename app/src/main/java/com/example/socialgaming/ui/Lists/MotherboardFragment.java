package com.example.socialgaming.ui.Lists;

import static com.example.socialgaming.data.types.ComponentType.MOTHERBOARD;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialgaming.R;
import com.example.socialgaming.data.ComponentBase;
import com.example.socialgaming.data.Motherboard;
import com.example.socialgaming.data.types.ComponentType;
import com.example.socialgaming.ui.Lists.placeholder.PlaceholderContent;
import com.example.socialgaming.utils.BuildUtils;

import java.util.ArrayList;
import java.util.List;

public class MotherboardFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private BuildUtils bu;
    private List<String> mbs;
    private String list;

    public MotherboardFragment() {
    }

    @SuppressWarnings("unused")
    public static MotherboardFragment newInstance(int columnCount) {
        MotherboardFragment fragment = new MotherboardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_motherboard_list, container, false);

        bu = new BuildUtils();
        mbs = new ArrayList<String>();
        Motherboard mb = new Motherboard();

        for(int i=0; i<50; i++){
            if(bu.getComponentsJSON(MOTHERBOARD, i, 0)!=null){
                mbs.add(bu.getComponentsJSON(MOTHERBOARD, i, 0));
            }
        }

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyMotherboardRecyclerViewAdapter(PlaceholderContent.ITEMS));
        }
        return view;
    }
}