package com.example.socialgaming.ui.ranks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialgaming.databinding.FragmentRanksBinding;

public class RanksFragment extends Fragment {

    private FragmentRanksBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        RanksViewModel ranksvm = new ViewModelProvider(this).get(RanksViewModel.class);
        binding = FragmentRanksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView tv = binding.textRanks;
        ranksvm.getText().observe(getViewLifecycleOwner(), tv::setText);
        return root;
    }

    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }

}
