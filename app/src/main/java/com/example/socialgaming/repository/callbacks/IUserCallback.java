package com.example.socialgaming.repository.callbacks;

import android.graphics.Bitmap;

import com.google.firebase.firestore.DocumentSnapshot;

public interface IUserCallback {
    void onUserReceived(DocumentSnapshot documentSnapshot);
    void onImageReceived(Bitmap image);
}
