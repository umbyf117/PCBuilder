package com.example.socialgaming.ui.Search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> Text;

    public SearchViewModel(){
        Text = new MutableLiveData<>();
        Text.setValue("Search Fragment");
    }

    public LiveData<String> getText(){
        return Text;
    }
}
