package com.example.socialgaming.repository.callbacks;

import com.google.firebase.firestore.DocumentSnapshot;

public interface IUserCallback {
    void onUserReceived(DocumentSnapshot documentSnapshot);
}
