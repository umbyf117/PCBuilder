package com.example.socialgaming.ui.Build;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.socialgaming.databinding.FragmentComponentsBinding;

public class BuildFragment extends Fragment {
    private FragmentComponentsBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        BuildViewModel bdvm = new ViewModelProvider(this).get(BuildViewModel.class);
        binding = FragmentComponentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;

    }

    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}
