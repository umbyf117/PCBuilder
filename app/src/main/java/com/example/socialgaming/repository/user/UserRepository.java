package com.example.socialgaming.repository.user;

import android.app.Application;
import android.net.Uri;
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

    //AGGIORNA LA PASSWORD SSE LA VECCHIA PASSWORD E oldPass COINCIDONO
    public boolean updatePassword(User user, String oldPass, String newPass) {
        documentReference = firestore.collection(USERS_COLLECTION).document(user.getUsername());

        if(user.changePassword(oldPass, newPass) != 0)
            return false;

        Map<String, Object> dataUpdated = new HashMap<>();
        dataUpdated.put("password", user.getPassword());
        Map<String, Object> upload = new HashMap<>();
        upload.put("/users/" + user.getUsername(), dataUpdated);

        database.updateChildren(upload);

        return true;

    }

    public boolean updateImage(User user, Uri image) {
        documentReference = firestore.collection(USERS_COLLECTION).document(user.getUsername());

        Map<String, Object> dataUpdated = new HashMap<>();
        dataUpdated.put("image", image);
        Map<String, Object> upload = new HashMap<>();
        upload.put("/users/" + user.getUsername(), dataUpdated);

        database.updateChildren(upload);

        return true;
    }

}
