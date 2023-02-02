package com.example.socialgaming.repository.user;

import android.app.Application;
import android.util.Log;

import com.example.socialgaming.data.User;
import com.example.socialgaming.repository.callback.AuthenticationCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private static final String USERS_COLLECTION = "users";

    private Application application;
    private AuthenticationCallback callback;

    private FirebaseFirestore firestore;
    private DatabaseReference database;
    private DocumentReference documentReference;

    private Map<String, Object> data;

    public UserRepository(Application application, AuthenticationCallback callback, String component) {
        this.application = application;
        this.callback = callback;
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }

    //DA FINIRE
    public boolean updatePassword(User user, String oldPass, String newPass) {
        documentReference = firestore.collection(USERS_COLLECTION).document(user.getUsername());

        if(user.changePassword(oldPass, newPass) != 0)
            return false;

        Map<String, Object> upload = new HashMap<>();
        data.put("password", user.getPassword());
        final boolean[] success = new boolean[1];

        firestore.collection(USERS_COLLECTION).add(upload)
                .addOnSuccessListener(documentReference -> {
                    Log.d("TAG", "Document written with ID: " + documentReference.getId());
                    success[0] = true;
                })
                .addOnFailureListener(e -> {
                    Log.w("TAG", "Error adding document", e);
                    success[0] = false;
                });

        return success[0];

    }

}
