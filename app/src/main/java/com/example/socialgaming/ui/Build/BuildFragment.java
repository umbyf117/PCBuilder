package com.example.socialgaming.ui.Build;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.socialgaming.R;
import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.CPU;
import com.example.socialgaming.data.CPUFan;
import com.example.socialgaming.data.Case;
import com.example.socialgaming.data.GPU;
import com.example.socialgaming.data.Memory;
import com.example.socialgaming.data.Motherboard;
import com.example.socialgaming.data.PSU;
import com.example.socialgaming.data.RAM;
import com.example.socialgaming.data.User;
import com.example.socialgaming.databinding.FragmentBuildBinding;
import com.example.socialgaming.databinding.FragmentProfileBinding;
import com.example.socialgaming.repository.component.BuildRepository;
import com.example.socialgaming.ui.Search.SearchFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class BuildFragment extends Fragment {

    private Scanner sc;
    private ListView lista;
    private ArrayList<String> arrayList;
    private androidx.appcompat.widget.SearchView cpusearch;
    private ArrayAdapter<String> arrayAdapter;
    private FragmentBuildBinding binding;
    private androidx.appcompat.widget.SearchView sv1, sv2, sv3, sv4;
    private Button btn1, btn2, btn3, btn4;
    private BuildRepository br;
    //Lists of components gotten through JSON
    private List<String> buildlist;
    private List<String> caselist;
    private List<String> complist;
    private List<String> cpulist;
    private List<String> cpufanlist;
    private List<String> gpulist;
    private List<String> memorylist;
    private List<String> mobolist;
    private List<String> psulist;
    private List<String> ramlist;
    private List<String> userlist;
    //Parts to search to create a Build object
    private Case house;
    private CPU cpu;
    private Motherboard motherboard;
    private RAM ram;
    private Memory memory;
    private GPU gpu;
    private CPUFan cpuFan;
    private PSU psu;
    //Secondary objects
    private User user;
    private String like;
    private String dislike;
    private UUID uuid;
    private String name;
    private Uri uri;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build, container, false);


        return view;
    }

    private void refreshView(){

    }

    private void createObjWithArgs(Motherboard motherboard, CPU cpu, List<RAM> ram, List<Memory> memories, GPU gpu, Case house, CPUFan fan, PSU psu, User creator, String name, Uri image){
        Build build = new Build (motherboard, cpu, ram, memories, gpu, house, fan, psu, creator, name, image);
    }

}
