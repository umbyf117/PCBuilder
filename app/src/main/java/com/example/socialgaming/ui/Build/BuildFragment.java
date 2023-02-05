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
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
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
import com.example.socialgaming.view.MainActivity;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class BuildFragment extends Fragment {

    private BuildViewModel bvm;
    private androidx.cardview.widget.CardView cv1;
    private TextView selectedDataTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bvm = new ViewModelProvider(this).get(BuildViewModel.class);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build, container, false);

        MainActivity activity = (MainActivity) getActivity();
        activity.setNightMode(AppCompatDelegate.getDefaultNightMode());

        cv1 = view.findViewById(R.id.moboBuild);
        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToMotherBoardFragment(50);
            }
        });

        return view;
    }

    private void switchToMotherBoardFragment(int numCardViews){
        Bundle bl = new Bundle();
        bl.putInt("numCardViews", numCardViews);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        MotherboardFragment mbf = new MotherboardFragment();
        mbf.setArguments(bl);
        ft.replace(R.id.buildViewLLayout, mbf)
                .addToBackStack(null)
                .commit();

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
