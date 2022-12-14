package com.example.socialgaming.ui.ranks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RanksViewModel extends ViewModel {

    private final MutableLiveData<String> Text;

    public RanksViewModel(){
        Text = new MutableLiveData<>();
        Text.setValue("Ranks Fragment");
    }

    public LiveData<String> getText(){
        return Text;
    }

}
