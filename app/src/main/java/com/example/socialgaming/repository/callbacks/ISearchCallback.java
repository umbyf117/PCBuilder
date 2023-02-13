package com.example.socialgaming.repository.callbacks;

import android.graphics.Bitmap;

import com.example.socialgaming.data.BuildFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface ISearchCallback {

    void onSearch(List<DocumentSnapshot> documents);
    void onImageReceived(Bitmap decodeByteArray, BuildFirestore build);
}
