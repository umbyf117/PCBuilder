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

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.socialgaming.ui.Lists.MotherboardFragment;
import com.example.socialgaming.ui.Search.SearchFragment;
import com.example.socialgaming.utils.BuildUtils;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class BuildFragment extends Fragment {

    private BuildViewModel bvm;
    private Button btn;
    private androidx.cardview.widget.CardView cv1, cv2, cv3, cv4, cv5, cv6, cv7, cv8;
    //Lists of components gotten through JSON
    private List<Build> buildlist;
    private List<Case> caselist;
    private List<CPU> cpulist;
    private List<CPUFan> cpufanlist;
    private List<GPU> gpulist;
    private List<Memory> memorylist;
    private List<Motherboard> mobolist;
    private List<PSU> psulist;
    private List<RAM> ramlist;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bvm = new ViewModelProvider(this).get(BuildViewModel.class);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build, container, false);
        ArrayList<Motherboard> mbs = new ArrayList<Motherboard>();

        cv1 = view.findViewById(R.id.moboBuild);
        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                MotherboardFragment mf = new MotherboardFragment();
                ft.add(R.id.buildViewLLayout, mf);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }

    private void saveSelectedItem(String selectedItem){
        bvm.setSelectedItem(selectedItem);
    }

    public String getSelectedItem(){
        return bvm.getSelectedItem();
    }

    private void createObjWithArgs(Motherboard motherboard, CPU cpu, List<RAM> ram, List<Memory> memories, GPU gpu, Case house, CPUFan fan, PSU psu, User creator, String name, Uri image){
        Build build = new Build (motherboard, cpu, ram, memories, gpu, house, fan, psu, creator, name, image);
    }

}
