package com.example.socialgaming.repository.user;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.socialgaming.data.User;
import com.example.socialgaming.repository.callbacks.IUserCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    public static final String USERS_COLLECTION = "users";

    private FirebaseFirestore firestore;
    private DatabaseReference database;
    private DocumentReference documentReference;

    private Map<String, Object> data;

    public UserRepository() {
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }


    //Ottenimento dati dell'utente
    public void getUserData(String username, IUserCallback callback) {
        documentReference = firestore.collection(USERS_COLLECTION).document("/" + username);
        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> callback.onUserReceived(documentSnapshot));

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

        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(user.getUsername());

        Map<String, Object> data = new HashMap<>();
        data.put("image", image);

        userRef.update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        return true;
        /*documentReference = firestore.collection(USERS_COLLECTION).document(user.getUsername());

        Map<String, Object> dataUpdated = new HashMap<>();
        dataUpdated.put("image", image);
        Map<String, Object> upload = new HashMap<>();
        upload.put("/users/" + user.getUsername(), dataUpdated);

        database.updateChildren(upload);

        return true;*/
    }

}
