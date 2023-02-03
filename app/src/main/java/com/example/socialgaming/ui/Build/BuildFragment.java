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
import java.util.List;

public class BuildFragment extends Fragment {

    ListView lista;
    ArrayList<String> arrayList;
    androidx.appcompat.widget.SearchView cpusearch;
    ArrayAdapter<String> arrayAdapter;
    private FragmentBuildBinding binding;
    /*
    private androidx.appcompat.widget.SearchView sv1, sv2, sv3, sv4;
    private List<String> buildsearch;
    private List<String> casesearch;
    private List<String> compsearch;
    private List<String> cpusearch;
    private List<String> cpufansearch;
    private List<String> gpusearch;
    private List<String> memory;
    private List<String> mobosearch;
    private List<String> psusearch;
    private List<String> ramsearch;
    private List<String> usersearch;
    */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build, container, false);

        lista = (ListView) view.findViewById(R.id.lista);

        arrayList = new ArrayList<>();
        arrayList.add("CPU1");
        arrayList.add("CPU2");
        arrayList.add("CPU3");
        arrayList.add("CPU4");
        arrayList.add("CPU5");
        arrayList.add("CPU6");
        arrayList.add("CPU7");

        cpusearch = (androidx.appcompat.widget.SearchView) view.findViewById(R.id.CPUSearch);

        arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, arrayList);

        lista.setAdapter(arrayAdapter);

        /*
        //Search CPU
        sv1 = root.findViewById(R.id.CPUSearch);
        sv1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<String> filteredDataSet = new ArrayList<>();
                for(String element : cpusearch){
                    if(element.toString().contains(query)){
                        filteredDataSet.add(element);
                    }
                }
                cpusearch = filteredDataSet;
                refreshView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //Search Motherboard
        sv2 = root.findViewById(R.id.moboSearch);
        sv2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<String> filteredDataSet = new ArrayList<>();
                for(String element : mobosearch){
                    if(element.toString().contains(query)){
                        filteredDataSet.add(element);
                    }
                }
                mobosearch = filteredDataSet;
                refreshView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //Search RAM
        sv3 = root.findViewById(R.id.ramSearch);
        sv3.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<String> filteredDataSet = new ArrayList<>();
                for(String element : ramsearch){
                    if(element.toString().contains(query)){
                        filteredDataSet.add(element);
                    }
                }
                ramsearch = filteredDataSet;
                refreshView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //Search GPU
        sv4 = root.findViewById(R.id.gpuSearch);
        sv4.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<String> filteredDataSet = new ArrayList<>();
                for(String element : gpusearch){
                    if(element.toString().contains(query)){
                        filteredDataSet.add(element);
                    }
                }
                gpusearch = filteredDataSet;
                refreshView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        */

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
        return view;
    }

    private void refreshView(){

    }

}
