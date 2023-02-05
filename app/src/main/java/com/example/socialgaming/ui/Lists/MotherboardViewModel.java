package com.example.socialgaming.ui.Lists;

import androidx.lifecycle.ViewModel;

public class MotherboardViewModel extends ViewModel {
    private String selectedItem;

    public String getSelectedItem(){
        return selectedItem;
    }

    public void setSelectedItem(String selectedItem){
        this.selectedItem = selectedItem;
    }
}
