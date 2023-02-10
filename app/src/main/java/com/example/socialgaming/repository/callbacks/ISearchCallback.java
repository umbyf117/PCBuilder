package com.example.socialgaming.repository.callbacks;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface ISearchCallback {

    void onSearch(List<DocumentSnapshot> documents);
}
