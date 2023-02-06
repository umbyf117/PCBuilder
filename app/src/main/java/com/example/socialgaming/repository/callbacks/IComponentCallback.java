package com.example.socialgaming.repository.callbacks;

import com.google.firebase.firestore.DocumentSnapshot;

public interface IComponentCallback {
    void onBuildReceived(String result);
}
