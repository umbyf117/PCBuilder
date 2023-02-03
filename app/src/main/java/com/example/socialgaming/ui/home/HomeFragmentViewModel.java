package com.example.socialgaming.ui.home;

import android.widget.LinearLayout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.socialgaming.data.Build;
import com.example.socialgaming.repository.component.BuildRepository;

public class HomeFragmentViewModel extends ViewModel {

    private static final int BUILD_PER_LOAD = 10;

    private final MutableLiveData<String> Text;
    private BuildRepository buildRepository;

    public HomeFragmentViewModel() {
        Text = new MutableLiveData<>();
        Text.setValue("Home Fragment");
        buildRepository = new BuildRepository();
    }

    public void addBuilds(LinearLayout linearLayout) {

    }

    public LiveData<String> getText(){
        return Text;
    }
}