package com.example.socialgaming.repository.callbacks;

import android.graphics.Bitmap;

import com.example.socialgaming.data.BuildFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface IBuildCallback {
    void onBuildReceived(DocumentSnapshot documentSnapshot, boolean created);
    void onBuildsReceived(List<DocumentSnapshot> documentsSnapshot, boolean created);
    void onImageReceived(Bitmap bitmap, BuildFirestore build, boolean created);
}
