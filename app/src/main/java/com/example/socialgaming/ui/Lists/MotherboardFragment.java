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
import com.example.socialgaming.data.Motherboard;
import com.example.socialgaming.utils.BuildUtils;

public class MotherboardFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private String list;
    private Motherboard mb;

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

        BuildUtils.getComponentsJSON(MOTHERBOARD, 50, 0);
        Motherboard[] mbs = (Motherboard[]) BuildUtils.getComponents(BuildUtils.getComponentsJSON(MOTHERBOARD, 50, 0), MOTHERBOARD);


        return view;
    }
}