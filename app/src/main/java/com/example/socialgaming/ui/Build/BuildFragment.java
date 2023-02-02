package com.example.socialgaming.ui.Build;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.socialgaming.R;
import com.example.socialgaming.databinding.FragmentBuildBinding;
import com.example.socialgaming.databinding.FragmentProfileBinding;
import com.example.socialgaming.ui.Search.SearchFragment;

import java.util.ArrayList;

public class BuildFragment extends Fragment {

    ListView lista;
    ArrayList<String> arrayList;
    androidx.appcompat.widget.SearchView cpusearch;
    ArrayAdapter<String> arrayAdapter;
    private FragmentBuildBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBuildBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lista = (ListView) root.findViewById(R.id.lista);

        arrayList = new ArrayList<>();
        arrayList.add("CPU1");
        arrayList.add("CPU2");
        arrayList.add("CPU3");
        arrayList.add("CPU4");
        arrayList.add("CPU5");
        arrayList.add("CPU6");
        arrayList.add("CPU7");

        cpusearch = (androidx.appcompat.widget.SearchView) root.findViewById(R.id.CPUSearch);

        arrayAdapter = new ArrayAdapter<String>(root.getContext(), android.R.layout.simple_list_item_1, arrayList);

        lista.setAdapter(arrayAdapter);


        cpusearch.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                arrayAdapter.getFilter().filter(newText);

                return false;
            }
        });
        return root;
    }
}
