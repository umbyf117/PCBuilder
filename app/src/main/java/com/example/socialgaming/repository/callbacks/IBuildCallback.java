package com.example.socialgaming.repository.callbacks;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface IBuildCallback {
    void onBuildReceived(DocumentSnapshot documentSnapshot);
    void onBuildsReceived(List<DocumentSnapshot> documentsSnapshot, boolean created);
}
